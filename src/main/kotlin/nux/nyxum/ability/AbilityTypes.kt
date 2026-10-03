package nux.nyxum.ability

data object Nothing : AbilityType
data class ActiveCommand(val command: String, override val keybind: String) : ActiveAbilityType