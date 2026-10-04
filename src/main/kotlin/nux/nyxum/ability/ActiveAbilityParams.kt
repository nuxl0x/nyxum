package nux.nyxum.ability

sealed interface ActiveAbilityParams : AbilityParams {
    val keybind: String
    val cooldown: Int get() = 0
}