package nux.nyxum.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.ClientActiveAbility
import nux.nyxum.client.power.ClientPowerCache

object AbilitySlotRenderer : HudRenderCallback {
    private val SLOT_TEXTURE = "textures/gui/slot.png".asId()

    override fun onHudRender(drawContext: GuiGraphics, tickDelta: Float) {
        val client = Minecraft.getInstance()

        if (client.options.hideGui) return

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        val screenWidth = client.window.guiScaledWidth
        val screenHeight = client.window.guiScaledHeight

        val font = client.font
        val activeAbilities = ClientPowerCache.getCachedActiveAbilities()

        val slotSize = 24
        val spacing = 4
        val slotCount = if (activeAbilities.size <= 5) activeAbilities.size else 5

        val totalSlotsHeight = (slotSize * slotCount) + (spacing * (slotCount - 1))

        val marginRight = 10
        val marginBottom = 55
        val x = screenWidth - slotSize - marginRight
        val initialY = screenHeight - marginBottom - totalSlotsHeight

        val icons = mutableListOf<ResourceLocation>()
        activeAbilities.forEach { ability -> icons.add(ability.icon) }

        for (i in 0 until slotCount) {
            val y = initialY + i * (slotSize + spacing)

            drawContext.blit(
                SLOT_TEXTURE,
                x, y,
                0f, 0f,
                slotSize, slotSize,
                slotSize, slotSize
            )
        }

        for (i in 0 until slotCount) {
            val y = initialY + i * (slotSize + spacing)

            val itemToRender = BuiltInRegistries.ITEM.get(icons[i])
            val renderItemStack = ItemStack(itemToRender, 1)

            drawContext.renderItem(renderItemStack, x + 4, y + 4)
        }

        for (i in 0 until slotCount) {
            val y = initialY + i * (slotSize + spacing)

            val ability = activeAbilities.getOrElse(i) { ClientActiveAbility() }
            val associatedKeybind = client.options.keyMappings.firstOrNull { it.name == ability.key }

            val keyText = associatedKeybind?.translatedKeyMessage ?: Component.literal("N/A")

            val textX = x + slotSize - font.width(keyText) - 2
            val textY = y + slotSize - 9

            drawContext.drawString(font, keyText, textX, textY, 0xFFFFFF, true)
        }

        // implement cooldown and toggle rendering

    }

}