package nux.nyxum.ability

import net.minecraft.server.level.ServerPlayer
import java.util.UUID

object AbilityStatusManager {

    private val abilityStatuses = mutableMapOf<UUID, MutableMap<Ability, AbilityStatus>>()

    fun setAbilityStatus(player: ServerPlayer, ability: Ability, status: AbilityStatus) {
        val playerAbilityStatuses = abilityStatuses.getOrPut(player.uuid) { mutableMapOf() }
        playerAbilityStatuses[ability] = status
    }

    fun isEnabled(player: ServerPlayer, ability: Ability): Boolean {
        val status = abilityStatuses[player.uuid]?.get(ability) ?: AbilityStatus.DISABLED
        return status == AbilityStatus.ENABLED
    }

    fun clearAbilityStatuses(player: ServerPlayer) {
        abilityStatuses.remove(player.uuid)
    }

}