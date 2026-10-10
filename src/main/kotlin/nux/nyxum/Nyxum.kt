package nux.nyxum

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import nux.nyxum.ability.AbilityStatusManager
import nux.nyxum.network.ActivateAbilityC2SPacket
import nux.nyxum.ability.CooldownManager
import nux.nyxum.ability.NyxumAbilityLoader
import nux.nyxum.power.NyxumPowerLoader
import nux.nyxum.ability.PassiveManager
import nux.nyxum.power.PlayerFlagManager
import nux.nyxum.ability.TickingPassiveManager
import nux.nyxum.power.PowerSync.syncPower
import nux.nyxum.registry.DataRegistry
import nux.nyxum.registry.PlayerPowerDataRegistry
import org.slf4j.LoggerFactory

object Nyxum : ModInitializer {
	const val MOD_ID: String = "nyxum"

	private val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOGGER.info("Initialising Nyxum...")
		DataRegistry.init()
		PlayerPowerDataRegistry.init()
		ActivateAbilityC2SPacket.init()

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(NyxumAbilityLoader())
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(NyxumPowerLoader())

		CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
			NyxumCommands.init(dispatcher)
		}

		ServerTickEvents.END_SERVER_TICK.register { server ->
			CooldownManager.tick()
			TickingPassiveManager.tick(server)
			PassiveManager.tick(server)
		}

		ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
			val player = handler.player
			TickingPassiveManager.addTickingPassives(player)
			PassiveManager.addPassives(player)
			player.syncPower()
		}

		ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
			val player = handler.player
			TickingPassiveManager.removeTickingPassives(player)
			PassiveManager.removePassives(player)
			CooldownManager.clearAllCooldowns(player)
			PlayerFlagManager.clearFlags(player)
			AbilityStatusManager.clearAbilityStatuses(player)
		}

		ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
			CooldownManager.clearAllCooldowns(newPlayer)
			newPlayer.syncPower()
		}
	}

	fun String.asId() = ResourceLocation(MOD_ID, this)
}
