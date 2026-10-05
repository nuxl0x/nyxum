package nux.nyxum.client.power

import nux.nyxum.power.PlayerFlag

object ClientFlagCache {
    private val cachedClientFlags = mutableMapOf<PlayerFlag, Boolean>()

    fun getFlag(flag: PlayerFlag): Boolean {
        return cachedClientFlags[flag] ?: false
    }

    fun setFlag(flag: PlayerFlag, value: Boolean) {
        cachedClientFlags[flag] = value
    }

    fun clearCachedFlags() {
        cachedClientFlags.clear()
    }
}