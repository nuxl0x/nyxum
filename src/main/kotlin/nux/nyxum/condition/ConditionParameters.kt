package nux.nyxum.condition

import net.minecraft.resources.ResourceLocation

data object None : ConditionParams

data class IsSubmerged(val fluid: ResourceLocation) : ConditionParams