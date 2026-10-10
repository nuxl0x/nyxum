package nux.nyxum.ability

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.condition.Interval
import nux.nyxum.registry.AbilityRegistry
import java.util.UUID
import kotlin.collections.forEach

object TickingPassiveManager {

    private val tickingPassives = mutableMapOf<UUID, MutableMap<Ability, Int>>()

    fun addTickingPassives(player: ServerPlayer) {
        val power = NyxumAPI.getPower(player) ?: return
        val tickingAbilities = mutableMapOf<Ability, Int>()
        power.abilities.forEach { ability ->
            val conditions = ability.conditions
            if (conditions.containsKey("interval".asId())) {
                val intervalCondition = conditions["interval".asId()] as? Interval ?: return@forEach
                tickingAbilities[ability] = intervalCondition.interval
            }
        }
        val playerTickingPassives = tickingPassives.getOrPut(player.uuid) { mutableMapOf() }
        playerTickingPassives.putAll(tickingAbilities)
    }

    fun removeTickingPassives(player: ServerPlayer) {
        tickingPassives.remove(player.uuid)
    }

    fun tick(server: MinecraftServer) {
        tickingPassives.forEach { (uuid, abilities) ->
            val player = server.playerList.getPlayer(uuid) ?: return@forEach

            abilities.entries.forEach { entry ->
                val ability = entry.key
                val condition = ability.conditions["interval".asId()] as? Interval ?: return@forEach

                var newRemaining = entry.value - 1
                if (newRemaining <= 0) {
                    newRemaining = condition.interval

                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    registryAbility.execute(ability, player, server)
                }
                entry.setValue(newRemaining)
            }
        }
    }

}