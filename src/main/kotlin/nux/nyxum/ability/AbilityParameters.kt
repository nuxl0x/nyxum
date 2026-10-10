package nux.nyxum.ability

data object Nothing : AbilityParams
data object Toggle : AbilityParams
data class ModifyResource(val resourceId: String, val operation: String, val value: Int) : AbilityParams

data class Command(val command: String) : AbilityParams
data object NightVision : AbilityParams