package nux.nyxum.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.ability.AbilityStatusManager
import nux.nyxum.condition.ConditionParams
import nux.nyxum.condition.Interval
import nux.nyxum.condition.IsEnabled
import nux.nyxum.condition.IsSubmerged
import nux.nyxum.condition.KeyPressed
import nux.nyxum.condition.None
import nux.nyxum.condition.ResourceCondition

enum class ConditionRegistry(
    val id: ResourceLocation,
    val params: Class<out ConditionParams>,
) {
    NONE("none".asId(), None::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            val castedCondition = condition as None
            return false.handleInvert(castedCondition.inverted)
        }
    },
    KEY_PRESSED("key_pressed".asId(), KeyPressed::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            return true
        }
    },
    INTERVAL("interval".asId(), Interval::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            return true
        }
    },
    IS_ENABLED("is_enabled".asId(), IsEnabled::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            val castedCondition = condition as IsEnabled
            val requestedAbility = DataRegistry.ABILITIES[castedCondition.ability] ?: return false
            return AbilityStatusManager.isEnabled(player, requestedAbility)
        }
    },
    RESOURCE("resource".asId(), ResourceCondition::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            val castedCondition = condition as ResourceCondition
            val power = NyxumAPI.getPower(player) ?: return false
            val resource = power.resources[castedCondition.name] ?: return false

            return when (castedCondition.operation) {
                "<" -> resource.value < condition.value
                "<=" -> resource.value <= condition.value
                ">" -> resource.value > condition.value
                ">=" -> resource.value >= condition.value
                "==" -> resource.value == condition.value
                "!=" -> resource.value != condition.value
                else -> false
            }
        }
    },

    IS_SUBMERGED("is_submerged".asId(), IsSubmerged::class.java) {
        override fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean {
            val isSubmerged = condition as IsSubmerged
            val fluidState = player.level().getFluidState(player.blockPosition())

            if (fluidState.isEmpty) return false.handleInvert(isSubmerged.inverted)

            val currentFluidId = BuiltInRegistries.FLUID.getKey(fluidState.type)
            return (currentFluidId == isSubmerged.fluid).handleInvert(isSubmerged.inverted)
        }
    };

    abstract fun evaluate(condition: ConditionParams, server: MinecraftServer, player: ServerPlayer): Boolean

    companion object {
        private val map = ConditionRegistry.entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NONE

        private fun Boolean.handleInvert(inverted: Boolean): Boolean = if (inverted) !this else this
    }
}