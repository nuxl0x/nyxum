package nux.nyxum.power

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.condition.ConditionParams
import nux.nyxum.registry.AbilityRegistry
import nux.nyxum.registry.ConditionRegistry
import nux.nyxum.registry.DataRegistry
import org.slf4j.LoggerFactory
import kotlin.collections.forEach
import kotlin.collections.set

class NyxumAbilityLoader : SimpleJsonResourceReloadListener(GSON, "nyxum/abilities"), IdentifiableResourceReloadListener {

    companion object {
        private val LOGGER = LoggerFactory.getLogger("nyxum")
        val ID = "ability_loader".asId()

        private val CONDITIONS_MAP_TYPE = object : TypeToken<Map<ResourceLocation, ConditionParams>>() {}.type

        val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(ResourceLocation::class.java, ResourceLocation.Serializer())

            .registerTypeAdapter(CONDITIONS_MAP_TYPE, JsonDeserializer { json, _, context ->
                val jsonObject = json.asJsonObject
                val resultMap = mutableMapOf<ResourceLocation, ConditionParams>()

                jsonObject.entrySet().forEach { (idString, conditionJson) ->
                    val conditionId = ResourceLocation.tryParse(idString) ?: "none".asId()
                    val matchedCondition = ConditionRegistry.fromId(conditionId)

                    val paramsInstance: ConditionParams = context.deserialize(conditionJson, matchedCondition.params)
                    resultMap[conditionId] = paramsInstance
                }
                resultMap
            })

            .registerTypeAdapter(Ability::class.java, JsonDeserializer { json, _, context ->
                val jsonObject = json.asJsonObject

                val name = jsonObject.get("name")?.asString ?: ""
                val description = jsonObject.get("description")?.asString ?: ""
                val icon = jsonObject.get("icon")?.let { context.deserialize(it, ResourceLocation::class.java) } ?: ResourceLocation("air")

                val conditions = jsonObject.get("conditions")?.let {
                    context.deserialize<Map<ResourceLocation, ConditionParams>>(it, CONDITIONS_MAP_TYPE)
                } ?: mapOf()

                val typeStr = jsonObject.get("type")?.asString ?: "nyxum:nothing"
                val typeId = ResourceLocation.tryParse(typeStr) ?: "nothing".asId()
                val matchedAbilityType = AbilityRegistry.fromId(typeId)

                val typeParams: AbilityParams = context.deserialize(json, (matchedAbilityType.params))

                Ability(
                    name = name,
                    description = description,
                    icon = icon,
                    typeId = typeId,
                    conditions = conditions,
                    params = typeParams
                )
            }).create()
    }

    override fun getFabricId(): ResourceLocation = ID

    override fun apply(
        prepared: Map<ResourceLocation, JsonElement>,
        manager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        DataRegistry.ABILITIES.clear()

        prepared.forEach { (id, json) ->
            try {
                val ability: Ability? = GSON.fromJson(json, Ability::class.java)
                if (ability != null) {
                    DataRegistry.ABILITIES[id] = ability
                }
            } catch (e: Exception) {
                LOGGER.error("Failed to load ability for id $id: ${e.message}", e)
            }
        }
    }
}
