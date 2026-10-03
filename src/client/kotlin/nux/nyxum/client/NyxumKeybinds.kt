package nux.nyxum.client

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.KeyMapping
import nux.nyxum.network.Packets
import org.lwjgl.glfw.GLFW

object NyxumKeybinds {
    val ABILITY_1 = KeyMapping(
        "key.nyxum.ability_1",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_Z,
        "category.nyxum.general"
    )

    val ABILITY_2 = KeyMapping(
        "key.nyxum.ability_2",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        "category.nyxum.general"
    )

    val ABILITY_3 = KeyMapping(
        "key.nyxum.ability_3",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_C,
        "category.nyxum.general"
    )

    val ABILITY_4 = KeyMapping(
        "key.nyxum.ability_4",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_V,
        "category.nyxum.general"
    )

    val ABILITY_5 = KeyMapping(
        "key.nyxum.ability_5",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        "category.nyxum.general"
    )

    fun init() {
        val binds = listOf(ABILITY_1, ABILITY_2, ABILITY_3, ABILITY_4, ABILITY_5)
        binds.forEach { KeyBindingHelper.registerKeyBinding(it) }

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (ABILITY_1.consumeClick()) {
                client.player?.let { _ ->
                    val buf = PacketByteBufs.create()
                    buf.writeInt(1)
                    ClientPlayNetworking.send(Packets.ACTIVATE_ABILITY_C2S, buf)
                }
            }
        }
    }
}