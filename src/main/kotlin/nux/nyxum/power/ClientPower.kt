package nux.nyxum.power

import net.minecraft.resources.ResourceLocation
import nux.nyxum.ability.ClientAbility
import nux.nyxum.ability.ClientActiveAbility

/**
 * Exists because it has to have a list of [ClientAbility] instead of normal so I couldn't re-use [Power].
 */
data class ClientPower(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    val abilityIds: List<ResourceLocation> = listOf(),
    val resources: Map<String, Resource> = mapOf(),
    val shouldDisplayPowerUI: Boolean = false, // false by default because of certain implementations in the client cache
    val abilities: MutableList<ClientAbility> = mutableListOf(),
    val activeAbilities: MutableList<ClientActiveAbility> = mutableListOf()
)
