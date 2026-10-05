package nux.nyxum.power

import net.minecraft.server.MinecraftServer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.registry.AbilityRegistry

object PassiveManager {
    private var tickCounter = 0

    fun tick(server: MinecraftServer) {
        tickCounter++

        if (tickCounter >= 10) {
            tickCounter = 0
            val players = server.playerList.players
            players.forEach { player ->
                val power = NyxumAPI.getPower(player) ?: return@forEach
                val passiveAbilities = power.abilities.filter { !it.conditions.containsKey("key_pressed".asId()) && !it.conditions.containsKey("interval".asId()) }

                if (passiveAbilities.isEmpty()) return@forEach
                passiveAbilities.forEach { ability ->
                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    registryAbility.abilityAction.invoke(ability, server, player)
                }
            }
        }
    }

}