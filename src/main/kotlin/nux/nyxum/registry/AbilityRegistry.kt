package nux.nyxum.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.ability.Command
import nux.nyxum.ability.NightVision
import nux.nyxum.ability.Nothing
import nux.nyxum.ability.Ability
import nux.nyxum.ability.AbilityStatus
import nux.nyxum.ability.AbilityStatusManager
import nux.nyxum.ability.Toggle
import nux.nyxum.power.PlayerFlag
import nux.nyxum.power.PlayerFlagManager

enum class AbilityRegistry(
    val id: ResourceLocation,
    val params: Class<out AbilityParams>,
    val abilityAction: (Ability, MinecraftServer, ServerPlayer) -> Unit
) {
    NOTHING("nothing".asId(), Nothing::class.java, { ability, _, player ->
        AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
    }),
    TOGGLE("toggle".asId(), Toggle::class.java, { ability, _, player ->
        val isToggled = AbilityStatusManager.isEnabled(player, ability)
        if (isToggled) {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        } else {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)
        }
    }),


    COMMAND("command".asId(), Command::class.java, { ability, server, player ->
        if (ability.shouldExecute(server, player)) {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)
            val command = ability.params as Command
            val commandsManager = server.commands
            val sourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput()

            commandsManager.performPrefixedCommand(sourceStack, command.command)
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        }
    }),
    NIGHT_VISION("night_vision".asId(), NightVision::class.java, { ability, server, player ->
        val shouldHaveNightVision = ability.shouldExecute(server, player)
        PlayerFlagManager.setFlag(player, PlayerFlag.HAS_NIGHT_VISION, shouldHaveNightVision)
        if (shouldHaveNightVision) {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)
        } else {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        }
    });

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NOTHING
    }
}