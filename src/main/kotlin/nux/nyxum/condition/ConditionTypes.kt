package nux.nyxum.condition

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

data object None : ConditionType

data class IsSubmerged(val fluid: ResourceLocation) : ConditionType