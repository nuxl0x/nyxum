package nux.nyxum.client.power

import nux.nyxum.ability.ClientActiveAbility

object ClientToggleManager {
    private val toggles = mutableMapOf<ClientActiveAbility, Boolean>()

    fun toggle(ability: ClientActiveAbility) {
        toggles[ability] = !(toggles[ability] ?: false)
    }

    fun isToggled(ability: ClientActiveAbility): Boolean {
        return toggles[ability] ?: false
    }

    fun clearAllToggles() {
        toggles.clear()
    }
}