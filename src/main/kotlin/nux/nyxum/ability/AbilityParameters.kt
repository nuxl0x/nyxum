package nux.nyxum.ability

data object Nothing : AbilityParams
data object Toggle : AbilityParams
data class Command(val command: String) : AbilityParams
data object NightVision : AbilityParams