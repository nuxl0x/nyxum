package nux.nyxum.power

import com.google.gson.annotations.SerializedName
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.condition.ConditionParams
import nux.nyxum.registry.ConditionRegistry

data class Ability(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    @SerializedName("type")
    val typeId: ResourceLocation = "nothing".asId(),

    val conditions: Map<ResourceLocation, ConditionParams> = mapOf(),

    val params: AbilityParams
) {
    fun shouldExecute(server: MinecraftServer, player: ServerPlayer): Boolean {
        return conditions.all { (id, condition) ->
            val registryCondition = ConditionRegistry.fromId(id)
            registryCondition.evaluate(condition, server, player)
        }
    }
}
