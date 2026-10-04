package nux.nyxum.ability

data object Nothing : AbilityParams
data class ActiveCommand(val command: String, override val keybind: String, override val cooldown: Int) : ActiveAbilityParams