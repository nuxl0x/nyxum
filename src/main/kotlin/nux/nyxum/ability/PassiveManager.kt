package nux.nyxum.ability

import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.registry.AbilityRegistry
import java.util.UUID

object PassiveManager {
    private var tickCounter = 0
    private val passives = mutableMapOf<UUID, List<Ability>>()

    fun addPassives(player: ServerPlayer) {
        val power = NyxumAPI.getPower(player) ?: return
        val passiveAbilities = power.abilities.filter { !it.conditions.containsKey("key_pressed".asId()) && !it.conditions.containsKey("interval".asId()) }
        passives[player.uuid] = passiveAbilities
    }

    fun removePassives(player: ServerPlayer) {
        passives.remove(player.uuid)
    }

    fun tick(server: MinecraftServer) {
        tickCounter++

        if (tickCounter >= 2) {
            tickCounter = 0
            passives.forEach { (uuid, abilities) ->
                val player = server.playerList.getPlayer(uuid) ?: return@forEach
                if (abilities.isEmpty()) return@forEach

                abilities.forEach { ability ->
                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    registryAbility.execute(ability, player, server)
                }
            }
        }
    }

}