package nux.nyxum.power

import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.condition.KeyPressed
import java.util.UUID

object CooldownManager {
    private val cooldowns = mutableMapOf<UUID, MutableMap<Ability, Int>>()

    fun isOnCooldown(player: ServerPlayer, ability: Ability): Boolean {
        return (cooldowns[player.uuid]?.get(ability) ?: 0) > 0
    }

    fun startCooldown(player: ServerPlayer, ability: Ability) {
        val playerCooldowns = cooldowns.getOrPut(player.uuid) { mutableMapOf() }
        val keyPressedCondition = ability.conditions["key_pressed".asId()] as? KeyPressed ?: return
        playerCooldowns[ability] = keyPressedCondition.cooldown
    }

    fun cancelCooldown(player: ServerPlayer, ability: Ability) {
        val playerCooldowns = cooldowns[player.uuid] ?: return
        playerCooldowns.remove(ability)

        if (playerCooldowns.isEmpty()) {
            cooldowns.remove(player.uuid)
        }
    }

    fun clearAllCooldowns(player: ServerPlayer) {
        cooldowns.remove(player.uuid)
    }

    fun getRemainingCooldownDuration(player: ServerPlayer, ability: Ability): Int {
        return cooldowns[player.uuid]?.get(ability)?.coerceAtLeast(0) ?: 0
    }

    fun tick() {
        cooldowns.forEach { (_, playerCooldowns) ->
            playerCooldowns.entries.removeIf { entry ->
                entry.setValue(entry.value - 1)
                entry.value <= 0
            }
        }

        cooldowns.entries.removeIf { it.value.isEmpty() }
    }
}