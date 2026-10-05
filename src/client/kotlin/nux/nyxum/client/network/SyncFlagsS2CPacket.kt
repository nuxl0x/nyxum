package nux.nyxum.client.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import nux.nyxum.client.power.ClientFlagCache
import nux.nyxum.network.Packets
import nux.nyxum.power.PlayerFlag

object SyncFlagsS2CPacket {

    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(Packets.SYNC_FLAGS_S2C) { client, _, buf, _ ->
            val flag = buf.readEnum(PlayerFlag::class.java)
            val value = buf.readBoolean()

            client.execute {
                ClientFlagCache.setFlag(flag, value)
            }
        }
    }

}