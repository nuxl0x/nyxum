package nux.nyxum.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.ability.ActiveCommand
import nux.nyxum.ability.Nothing

enum class AbilityRegistry(
    val id: ResourceLocation,
    val type: Class<out AbilityParams>,
    val abilityAction: (AbilityParams, MinecraftServer, ServerPlayer) -> Unit
) {
    NOTHING("nothing".asId(), Nothing::class.java, { _, _, _ -> }),
    COMMAND("active_command".asId(), ActiveCommand::class.java, { commandType, server, player ->
        val command = commandType as ActiveCommand
        val commandsManager = server.commands
        val sourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput()

        commandsManager.performPrefixedCommand(sourceStack, command.command)
    });

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NOTHING
    }
}