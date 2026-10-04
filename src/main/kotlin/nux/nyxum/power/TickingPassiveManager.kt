package nux.nyxum.power

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.NyxumAPI
import nux.nyxum.ability.TickingAbilityParams
import nux.nyxum.registry.AbilityRegistry
import nux.nyxum.registry.ConditionRegistry
import java.util.UUID

object TickingPassiveManager {

    private val tickingPassives = mutableMapOf<UUID, MutableMap<Ability, Int>>()

    fun addTickingPassives(player: ServerPlayer) {
        val power = NyxumAPI.getPower(player) ?: return
        val tickingAbilities = mutableMapOf<Ability, Int>()
        power.abilities.forEach { ability ->
            val params = ability.params
            if (ability.params is TickingAbilityParams) {
                tickingAbilities[ability] = params.interval
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
                val params = ability.params as? TickingAbilityParams ?: return@forEach

                var newRemaining = entry.value - 1
                if (newRemaining <= 0) {
                    newRemaining = params.interval

                    val conditionsMet = ability.conditions.all { (id, condition) ->
                        val registryCondition = ConditionRegistry.fromId(id)
                        registryCondition.evaluate(condition, server, player)
                    }

                    if (!conditionsMet) return@forEach

                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    // Do not use params variable, as it is already cast!
                    registryAbility.abilityAction.invoke(ability.params, server, player)
                }
                entry.setValue(newRemaining)
            }
        }
    }

}