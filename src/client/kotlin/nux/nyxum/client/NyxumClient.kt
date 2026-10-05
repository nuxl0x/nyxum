package nux.nyxum.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import nux.nyxum.client.network.SyncFlagsS2CPacket
import nux.nyxum.client.power.ClientFlagCache

object NyxumClient : ClientModInitializer {
	override fun onInitializeClient() {
		NyxumKeybinds.init()
		SyncFlagsS2CPacket.init()

		ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
			ClientFlagCache.clearCachedFlags()
		}
	}
}