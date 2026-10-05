package nux.nyxum.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.condition.KeyPressed
import nux.nyxum.ability.Ability
import nux.nyxum.ability.AbilityStatus
import nux.nyxum.ability.AbilityStatusManager
import nux.nyxum.ability.CooldownManager
import nux.nyxum.registry.AbilityRegistry

object ActivateAbilityC2SPacket {

    fun init() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.ACTIVATE_ABILITY_C2S) { server, player, _, buf, _ ->
            val abilityNumber = buf.readInt()
            server.execute {
                val power = NyxumAPI.getPower(player) ?: return@execute
                val abilities = power.abilities
                val keybindKey = "key.nyxum.ability_$abilityNumber"

                val toTrigger = mutableListOf<Ability>()
                abilities.forEach { ability ->
                    val keyPressedCondition = ability.conditions["key_pressed".asId()] as? KeyPressed ?: return@forEach
                    if (keyPressedCondition.key == keybindKey) {
                        toTrigger.add(ability)
                    }
                }

                if (toTrigger.isEmpty()) return@execute

                toTrigger.forEach { ability ->
                    if (CooldownManager.isOnCooldown(player, ability)) return@forEach

                    CooldownManager.startCooldown(player, ability)

                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    registryAbility.abilityAction.invoke(ability, server, player)
                }

            }
        }
    }

}