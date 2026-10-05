package nux.nyxum.power

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.network.Packets
import java.util.UUID

object PlayerFlagManager {
    private val playerFlags = mutableMapOf<UUID, MutableMap<PlayerFlag, Boolean>>()

    fun setFlag(player: ServerPlayer, flag: PlayerFlag, value: Boolean) {
        val flags = playerFlags.getOrPut(player.uuid) { mutableMapOf() }
        flags[flag] = value

        // Sync the flags with the client cache. Client cache will clear itself on disconnection. The server is the truth.
        val buf = PacketByteBufs.create()
        buf.writeEnum(flag)
        buf.writeBoolean(value)
        ServerPlayNetworking.send(player, Packets.SYNC_FLAGS_S2C, buf)
    }

    fun getFlag(player: ServerPlayer, flag: PlayerFlag): Boolean {
        return playerFlags[player.uuid]?.get(flag) ?: false
    }

    fun clearFlags(player: ServerPlayer) {
        playerFlags.remove(player.uuid)
    }

}