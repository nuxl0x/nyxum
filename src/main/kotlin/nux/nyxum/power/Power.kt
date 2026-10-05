package nux.nyxum.power

import com.google.gson.annotations.SerializedName
import net.minecraft.resources.ResourceLocation
import nux.nyxum.ability.Ability

data class Power(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    @SerializedName("abilities")
    val abilityIds: List<ResourceLocation> = listOf(),
    val resources: Map<String, Resource> = mapOf(),
    @SerializedName("show_power_ui")
    val shouldDisplayPowerUI: Boolean = true,

    @Transient
    val abilities: MutableList<Ability> = mutableListOf()
)
