package nux.nyxum

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.power.Power
import nux.nyxum.power.PowerManager
import nux.nyxum.registry.DataRegistry

object NyxumAPI {

    /**
     * [grantPower] returns a boolean true if it makes the call to PowerManager.
     */
    fun grantPower(player: ServerPlayer, powerId: ResourceLocation): Boolean {
        if (PowerManager.getPower(player) != null) return false // Fails if the player already has a power.
        PowerManager.setPower(player, powerId)
        return true
    }

    fun setPower(player: ServerPlayer, powerId: ResourceLocation) {
        PowerManager.setPower(player, powerId)
    }

    /**
     * [revokePower] returns a boolean true if it makes the call to PowerManager.
     */
    fun revokePower(player: ServerPlayer): Boolean {
        if (PowerManager.getPower(player) == null) return false// Fails if the player doesn't have a power.
        PowerManager.clearPower(player)
        return true
    }

    fun getPower(player: ServerPlayer): Power? = PowerManager.getPower(player)

    fun hasPower(player: ServerPlayer, toConfirmPowerId: ResourceLocation): Boolean {
        val currentPower = getPower(player) ?: return false
        val currentPowerId = DataRegistry.POWERS.entries.find { it.value == currentPower }?.key ?: return false
        return currentPowerId == toConfirmPowerId
    }

}