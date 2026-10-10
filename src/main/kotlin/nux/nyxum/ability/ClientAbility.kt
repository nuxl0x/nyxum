package nux.nyxum.ability

import net.minecraft.resources.ResourceLocation
import nux.nyxum.Nyxum.asId

/**
 * Used exclusively for sending ability data to client for syncing information.
 * Had to be made extra due to the fact that you can't really write the sealed interfaces to [net.minecraft.network.FriendlyByteBuf].
 */
data class ClientAbility(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    val typeId: ResourceLocation = "nothing".asId(),
)
