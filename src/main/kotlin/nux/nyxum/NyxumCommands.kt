package nux.nyxum

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.network.chat.Component
import nux.nyxum.registry.DataRegistry

object NyxumCommands {

    fun init(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("nyxum")
                .requires { sourceStack -> sourceStack.hasPermission(2) }
                .then(
                    Commands.literal("grant")
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .then(
                                    Commands.argument("power", ResourceLocationArgument.id())
                                        .suggests { _, builder ->
                                            val powerIds = DataRegistry.POWERS.keys
                                            SharedSuggestionProvider.suggestResource(powerIds, builder)
                                        }
                                        .executes { context ->
                                            val target = EntityArgument.getPlayer(context, "target")
                                            val powerId = ResourceLocationArgument.getId(context, "power")

                                            val success = NyxumAPI.grantPower(target, powerId)
                                            if (!success) {
                                                context.source.sendFailure(
                                                    Component.literal("Target already has a power!")
                                                )
                                                return@executes 0
                                            }
                                            context.source.sendSuccess(
                                                { Component.literal("Granted '$powerId' to ${target.name.string}.") },
                                                true
                                            )
                                            1
                                        }
                                )
                        )
                )

                .then(
                    Commands.literal("revoke")
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .executes { context ->
                                    val target = EntityArgument.getPlayer(context, "target")

                                    val powerId = DataRegistry.POWERS.entries.find { it.value == NyxumAPI.getPower(target) }?.key
                                    if (powerId == null) {
                                        context.source.sendFailure(
                                            Component.literal("Target has no power to revoke!")
                                        )
                                        return@executes 0
                                    }
                                    NyxumAPI.revokePower(target)
                                    context.source.sendSuccess(
                                        { Component.literal("Revoked '$powerId' from ${target.name.string}.") },
                                        true
                                    )
                                    1
                                }
                        )
                )

                .then(
                    Commands.literal("set")
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .then(
                                    Commands.argument("power", ResourceLocationArgument.id())
                                        .suggests { _, builder ->
                                            val powerIds = DataRegistry.POWERS.keys
                                            SharedSuggestionProvider.suggestResource(powerIds, builder)
                                        }
                                        .executes { context ->
                                            val target = EntityArgument.getPlayer(context, "target")
                                            val powerId = ResourceLocationArgument.getId(context, "power")

                                            NyxumAPI.setPower(target, powerId)
                                            context.source.sendSuccess(
                                                { Component.literal("Set ${target.name.string}'s power to '$powerId'.") },
                                                true
                                            )
                                            1
                                        }
                                )
                        )
                )

                .then(
                    Commands.literal("get")
                        .then(
                            Commands.argument("target", EntityArgument.player())
                                .executes { context ->
                                    val target = EntityArgument.getPlayer(context, "target")

                                    val powerId = DataRegistry.POWERS.entries.find { it.value == NyxumAPI.getPower(target) }?.key
                                    if (powerId == null) {
                                        context.source.sendFailure(
                                            Component.literal("Target does not have a power!")
                                        )
                                        return@executes 0
                                    }
                                    context.source.sendSuccess(
                                        { Component.literal("${target.name.string} has power '$powerId'.") },
                                        true
                                    )
                                    1
                                }
                        )
                )
        )
    }
}