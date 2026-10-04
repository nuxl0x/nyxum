package nux.nyxum.condition

sealed interface ConditionParams {
    val inverted: Boolean get() = false
}