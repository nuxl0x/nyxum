package nux.nyxum.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.ability.AbilityParams
import nux.nyxum.ability.Command
import nux.nyxum.ability.NightVision
import nux.nyxum.ability.Nothing
import nux.nyxum.ability.Ability
import nux.nyxum.ability.AbilityStatus
import nux.nyxum.ability.AbilityStatusManager
import nux.nyxum.ability.ModifyResource
import nux.nyxum.ability.Toggle
import nux.nyxum.power.PlayerFlag
import nux.nyxum.power.PlayerFlagManager

enum class AbilityRegistry(
    val id: ResourceLocation,
    val params: Class<out AbilityParams>,
) {
    NOTHING("nothing".asId(), Nothing::class.java) {
        override fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer) {
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        }
    },
    TOGGLE("toggle".asId(), Toggle::class.java) {
        override fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer) {
            val isToggled = AbilityStatusManager.isEnabled(player, ability)
            if (isToggled) {
                AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
            } else {
                AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)
            }
        }
    },
    MODIFY_RESOURCE("modify_resource".asId(), ModifyResource::class.java) {
        override fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer) {
            if (!ability.shouldExecute(server, player)) return
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)

            val params = ability.params as ModifyResource
            val power = NyxumAPI.getPower(player) ?: return
            val toModify = power.resources[params.resourceId] ?: return

            when (params.operation) {
                "add" -> {
                    toModify.value + params.value
                }
                "set" -> {
                    toModify.value = params.value
                }
            }

            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        }
    },


    COMMAND("command".asId(), Command::class.java) {
        override fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer) {
            if (!ability.shouldExecute(server, player)) return
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)

            val params = ability.params as Command
            val commandsManager = server.commands
            val sourceStack = player.createCommandSourceStack().withPermission(4).withSuppressedOutput()

            commandsManager.performPrefixedCommand(sourceStack, params.command)
            AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
        }
    },
    NIGHT_VISION("night_vision".asId(), NightVision::class.java) {
        override fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer) {
            val shouldHaveNightVision = ability.shouldExecute(server, player)
            PlayerFlagManager.setFlag(player, PlayerFlag.HAS_NIGHT_VISION, shouldHaveNightVision)
            if (shouldHaveNightVision) {
                AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.ENABLED)
            } else {
                AbilityStatusManager.setAbilityStatus(player, ability, AbilityStatus.DISABLED)
            }
        }
    };

    abstract fun execute(ability: Ability, player: ServerPlayer, server: MinecraftServer)

    companion object {
        private val map = entries.associateBy { it.id }
        fun fromId(id: ResourceLocation) = map[id] ?: NOTHING
    }
}