package nux.nyxum.power

import com.google.gson.annotations.SerializedName
import net.minecraft.resources.ResourceLocation
import nux.nyxum.Nyxum.asId
import nux.nyxum.ability.AbilityParams
import nux.nyxum.condition.ConditionParams

data class Ability(
    val name: String = "",
    val description: String = "",
    val icon: ResourceLocation = ResourceLocation("air"),
    @SerializedName("type")
    val typeId: ResourceLocation = "nothing".asId(),

    val conditions: Map<ResourceLocation, ConditionParams> = mapOf(),

    val params: AbilityParams
)
