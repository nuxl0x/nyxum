package nux.nyxum.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import nux.nyxum.client.power.ClientPowerCache
import nux.nyxum.network.Packets
import nux.nyxum.power.PowerSync.readPower

object SyncPowerS2CPacket {

    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.SYNC_POWER_S2C) { client, _, buf, _ ->
            val power = buf.readPower()
            client.execute {
                ClientPowerCache.setCachedPower(power)
            }
        }
    }

}