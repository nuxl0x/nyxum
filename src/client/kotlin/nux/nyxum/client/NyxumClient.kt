package nux.nyxum.client

import net.fabricmc.api.ClientModInitializer

object NyxumClient : ClientModInitializer {
	override fun onInitializeClient() {
		NyxumKeybinds.init()
	}
}