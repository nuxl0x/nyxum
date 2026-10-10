package nux.nyxum.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import nux.nyxum.client.power.ClientToggleManager
import nux.nyxum.network.Packets
import nux.nyxum.power.PowerSync.readActiveAbility

object ToggleAbilityS2CPacket {

    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.TOGGLE_ABILITY_S2C) { client, _, buf, _ ->
            val ability = buf.readActiveAbility()
            client.execute {
                ClientToggleManager.toggle(ability)
            }
        }
    }

}