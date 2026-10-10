package nux.nyxum.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import nux.nyxum.client.gui.AbilitySlotRenderer
import nux.nyxum.client.network.StartCooldownS2CPacket
import nux.nyxum.client.network.SyncFlagsS2CPacket
import nux.nyxum.client.network.SyncPowerS2CPacket
import nux.nyxum.client.network.ToggleAbilityS2CPacket
import nux.nyxum.client.power.ClientCooldownManager
import nux.nyxum.client.power.ClientFlagCache
import nux.nyxum.client.power.ClientPowerCache
import nux.nyxum.client.power.ClientToggleManager

object NyxumClient : ClientModInitializer {
	override fun onInitializeClient() {
		NyxumKeybinds.init()
		SyncFlagsS2CPacket.init()
		SyncPowerS2CPacket.init()
		StartCooldownS2CPacket.init()
		ToggleAbilityS2CPacket.init()

		ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
			ClientFlagCache.clearCachedFlags()
			ClientPowerCache.clearCachedPower()
			ClientCooldownManager.clearAllCooldowns()
			ClientToggleManager.clearAllToggles()
		}

		HudRenderCallback.EVENT.register(AbilitySlotRenderer)
	}
}