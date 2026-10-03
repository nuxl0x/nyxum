package nux.nyxum.power

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.registry.DataRegistry
import nux.nyxum.registry.PlayerPowerDataRegistry

object PowerManager {

    fun setPower(player: ServerPlayer, powerId: ResourceLocation) {
        if (!DataRegistry.POWERS.containsKey(powerId)) return
        player.setAttached(PlayerPowerDataRegistry.POWER, powerId.toString())
    }

    fun getPower(player: ServerPlayer): Power? {
        val attachedPowerString = player.getAttached(PlayerPowerDataRegistry.POWER) ?: return null
        val attachedPowerId = ResourceLocation.tryParse(attachedPowerString) ?: return null

        return DataRegistry.POWERS[attachedPowerId]
    }

    fun clearPower(player: ServerPlayer) {
        if (getPower(player) == null) return
        player.removeAttached(PlayerPowerDataRegistry.POWER)
    }
}