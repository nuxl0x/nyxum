package nux.nyxum.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.condition.ConditionType
import nux.nyxum.condition.IsSubmerged
import nux.nyxum.condition.None

enum class ConditionRegistry(
    val id: ResourceLocation,
    val type: Class<out ConditionType>,
) {
    NONE("none".asId(), None::class.java) {
        override fun evaluate(condition: ConditionType, server: MinecraftServer, player: ServerPlayer): Boolean {
            val none = condition as None
            return false.handleInvert(none.inverted)
        }
    },
    IS_SUBMERGED("is_submerged".asId(), IsSubmerged::class.java) {
        override fun evaluate(condition: ConditionType, server: MinecraftServer, player: ServerPlayer): Boolean {
            val isSubmerged = condition as IsSubmerged
            val fluidState = player.level().getFluidState(player.blockPosition())

            if (fluidState.isEmpty) return false.handleInvert(isSubmerged.inverted)

            val currentFluidId = BuiltInRegistries.FLUID.getKey(fluidState.type)
            return (currentFluidId == isSubmerged.fluid).handleInvert(isSubmerged.inverted)
        }
    };

    abstract fun evaluate(condition: ConditionType, server: MinecraftServer, player: ServerPlayer): Boolean

    companion object {
        private val map = ConditionRegistry.entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NONE

        private fun Boolean.handleInvert(inverted: Boolean): Boolean = if (inverted) !this else this
    }
}