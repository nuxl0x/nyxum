package nux.nyxum.power

import com.google.gson.annotations.SerializedName
import net.minecraft.resources.ResourceLocation
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityType
import nux.nyxum.condition.ConditionType

data class Ability(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    @SerializedName("type")
    val typeId: ResourceLocation = "nothing".asId(),

    val conditions: Map<ResourceLocation, ConditionType> = mapOf(),

    val type: AbilityType
)
