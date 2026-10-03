package nux.nyxum.ability

sealed interface ActiveAbilityType : AbilityType {
    val keybind: String
}