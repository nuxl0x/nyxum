package nux.nyxum.condition

import net.minecraft.resources.ResourceLocation

data object None : ConditionParams

data class KeyPressed(val key: String, val cooldown: Int) : ConditionParams
data class Interval(val interval: Int) : ConditionParams
data class IsEnabled(val ability: ResourceLocation) : ConditionParams
data class ResourceCondition(val name: String, val operation: String, val value: Int) : ConditionParams

data class IsSubmerged(val fluid: ResourceLocation) : ConditionParams