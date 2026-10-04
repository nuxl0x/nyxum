package nux.nyxum

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.ability.TickingAbilityParams
import nux.nyxum.power.CooldownManager
import nux.nyxum.power.Power
import nux.nyxum.power.PowerManager
import nux.nyxum.power.TickingPassiveManager
import nux.nyxum.registry.DataRegistry

object NyxumAPI {

    /**
     * [grantPower] returns a boolean true if it makes the call to PowerManager.
     */
    fun grantPower(player: ServerPlayer, powerId: ResourceLocation): Boolean {
        if (PowerManager.getPower(player) != null) return false // Fails if the player already has a power.
        setPower(player, powerId)
        return true
    }

    fun setPower(player: ServerPlayer, powerId: ResourceLocation) {
        TickingPassiveManager.removeTickingPassives(player)
        CooldownManager.clearAllCooldowns(player)

        PowerManager.setPower(player, powerId)
        TickingPassiveManager.addTickingPassives(player)
    }

    /**
     * [revokePower] returns a boolean true if it makes the call to PowerManager.
     */
    fun revokePower(player: ServerPlayer): Boolean {
        if (PowerManager.getPower(player) == null) return false // Fails if the player doesn't have a power.
        TickingPassiveManager.removeTickingPassives(player)
        CooldownManager.clearAllCooldowns(player)
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