package nux.nyxum.registry

import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import nux.nyxum.Nyxum.asId

object PlayerPowerDataRegistry {
    val POWER: AttachmentType<String> = AttachmentRegistry.builder<String>()
        .persistent(Codec.STRING)
        .copyOnDeath()
        .buildAndRegister("power".asId())

    fun init() {}
}