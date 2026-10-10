package nux.nyxum.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import nux.nyxum.client.power.ClientCooldownManager
import nux.nyxum.network.Packets
import nux.nyxum.power.PowerSync.readActiveAbility

object StartCooldownS2CPacket {

    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.START_COOLDOWN_S2C) { client, _, buf, _ ->
            val ability = buf.readActiveAbility()
            client.execute {
                ClientCooldownManager.startCooldown(ability)
            }
        }
    }

}