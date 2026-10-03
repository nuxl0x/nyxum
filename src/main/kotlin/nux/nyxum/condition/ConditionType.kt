package nux.nyxum.condition

sealed interface ConditionType {
    val inverted: Boolean get() = false
}