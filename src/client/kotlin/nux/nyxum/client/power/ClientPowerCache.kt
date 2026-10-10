package nux.nyxum.client.power

import nux.nyxum.ability.ClientAbility
import nux.nyxum.ability.ClientActiveAbility
import nux.nyxum.power.ClientPower

object ClientPowerCache {
    private var cachedPower = ClientPower()

    fun setCachedPower(power: ClientPower) {
        cachedPower = power
    }

    fun getCachedPower(): ClientPower {
        return cachedPower
    }

    fun getCachedAbilities(): List<ClientAbility> {
        return cachedPower.abilities
    }

    fun getCachedActiveAbilities(): List<ClientActiveAbility> {
        return cachedPower.activeAbilities
    }

    fun clearCachedPower() {
        cachedPower = ClientPower()
    }

}