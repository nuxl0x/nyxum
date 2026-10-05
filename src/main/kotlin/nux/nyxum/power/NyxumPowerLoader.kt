package nux.nyxum.power

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.NyxumAbilityLoader
import nux.nyxum.registry.DataRegistry
import org.slf4j.LoggerFactory

class NyxumPowerLoader : SimpleJsonResourceReloadListener(GSON, "nyxum/powers"), IdentifiableResourceReloadListener {

    companion object {
        private val LOGGER = LoggerFactory.getLogger("nyxum")
        private val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(ResourceLocation::class.java, ResourceLocation.Serializer())
            .create()
        val ID = "power_loader".asId()
    }

    override fun getFabricId(): ResourceLocation = ID

    override fun getFabricDependencies(): Collection<ResourceLocation> = listOf(NyxumAbilityLoader.ID)

    override fun apply(
        prepared: Map<ResourceLocation, JsonElement>,
        manager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        DataRegistry.POWERS.clear()

        prepared.forEach { (id, json) ->
            try {
                val power: Power = GSON.fromJson(json, Power::class.java) ?: return

                power.abilityIds.forEach { abilityId ->
                    val associatedAbility = DataRegistry.ABILITIES[abilityId]
                    if (associatedAbility != null) {
                        power.abilities.add(associatedAbility)
                    } else {
                        LOGGER.warn("Could not find an ability with ID $abilityId! Skipping.")
                    }
                }

                DataRegistry.POWERS[id] = power
            } catch (e: Exception) {
                LOGGER.error("Failed to load power for id $id, ${e.message}")
            }
        }
    }

}