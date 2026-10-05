package nux.nyxum.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.ability.Command
import nux.nyxum.ability.NightVision
import nux.nyxum.ability.Nothing
import nux.nyxum.power.Ability
import nux.nyxum.power.PlayerFlag
import nux.nyxum.power.PlayerFlagManager

enum class AbilityRegistry(
    val id: ResourceLocation,
    val params: Class<out AbilityParams>,
    val abilityAction: (Ability, MinecraftServer, ServerPlayer) -> Unit
) {
    NOTHING("nothing".asId(), Nothing::class.java, { _, _, _ -> }),
    COMMAND("command".asId(), Command::class.java, { commandAbility, server, player ->
        if (commandAbility.shouldExecute(server, player)) {
            val command = commandAbility.params as Command
            val commandsManager = server.commands
            val sourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput()

            commandsManager.performPrefixedCommand(sourceStack, command.command)
        }
    }),
    NIGHT_VISION("night_vision".asId(), NightVision::class.java, { nightVisionAbility, server, player ->
        val shouldHaveNightVision = nightVisionAbility.shouldExecute(server, player)
        PlayerFlagManager.setFlag(player, PlayerFlag.HAS_NIGHT_VISION, shouldHaveNightVision)
    });

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NOTHING
    }
}