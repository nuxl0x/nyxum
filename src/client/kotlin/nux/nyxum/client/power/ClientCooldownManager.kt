package nux.nyxum.client.power

import nux.nyxum.ability.ClientActiveAbility

object ClientCooldownManager {
    private val cooldowns = mutableMapOf<ClientActiveAbility, Int>()

    fun isOnCooldown(ability: ClientActiveAbility): Boolean {
        return (cooldowns[ability] ?: 0) > 0
    }

    fun startCooldown(ability: ClientActiveAbility) {
        cooldowns[ability] = ability.cooldown
    }

    fun cancelCooldown(ability: ClientActiveAbility) {
        cooldowns.remove(ability)
    }

    fun clearAllCooldowns() {
        cooldowns.clear()
    }

    fun getRemainingCooldownDuration(ability: ClientActiveAbility): Int {
        return cooldowns[ability]?.coerceAtLeast(0) ?: 0
    }

    fun tick() {
        cooldowns.entries.removeIf { entry ->
            entry.setValue(entry.value - 1)
            entry.value <= 0
        }
    }
}