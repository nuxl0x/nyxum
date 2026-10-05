package nux.nyxum.registry

import net.minecraft.resources.ResourceLocation
import nux.nyxum.ability.Ability
import nux.nyxum.power.Power

object DataRegistry {
    val POWERS = mutableMapOf<ResourceLocation, Power>()
    val ABILITIES = mutableMapOf<ResourceLocation, Ability>()

    fun init() {}
}