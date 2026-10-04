package nux.nyxum.ability

sealed interface TickingAbilityParams : PassiveAbilityParams {
    val interval: Int
}