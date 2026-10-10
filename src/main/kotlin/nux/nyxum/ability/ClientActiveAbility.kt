package nux.nyxum.ability

import net.minecraft.resources.ResourceLocation
import nux.nyxum.Nyxum.asId

data class ClientActiveAbility(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    val typeId: ResourceLocation = "nothing".asId(),
    val key: String = "",
    val cooldown: Int = 0,
)
