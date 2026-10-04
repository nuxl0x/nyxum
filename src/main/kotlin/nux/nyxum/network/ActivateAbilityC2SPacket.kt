package nux.nyxum.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import nux.nyxum.NyxumAPI
import nux.nyxum.ability.ActiveAbilityParams
import nux.nyxum.power.Ability
import nux.nyxum.power.CooldownManager
import nux.nyxum.registry.AbilityRegistry
import nux.nyxum.registry.ConditionRegistry

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
                    if (ability.params is ActiveAbilityParams && ability.params.keybind == keybindKey) {
                        toTrigger.add(ability)
                    }
                }

                toTrigger.forEach { ability ->
                    val conditionsMet = ability.conditions.all { (id, condition) ->
                        val registryCondition = ConditionRegistry.fromId(id)
                        registryCondition.evaluate(condition, server, player)
                    }

                    if (!conditionsMet) return@forEach

                    if (CooldownManager.isOnCooldown(player, ability)) return@forEach

                    CooldownManager.startCooldown(player, ability)

                    val registryAbility = AbilityRegistry.fromId(ability.typeId)
                    registryAbility.abilityAction.invoke(ability.params, server, player)
                }

            }
        }
    }

}