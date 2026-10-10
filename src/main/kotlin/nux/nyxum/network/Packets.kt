package nux.nyxum.network

import nux.nyxum.Nyxum.asId

object Packets {
    // Client to Server packets.
    val ACTIVATE_ABILITY_C2S = "activate_ability_c2s".asId()

    // Server to Client packets.
    val SYNC_FLAGS_S2C = "sync_flags_s2c".asId()
    val SYNC_POWER_S2C = "sync_power_s2c".asId()
    val START_COOLDOWN_S2C = "start_cooldown_s2c".asId()
    val TOGGLE_ABILITY_S2C = "toggle_ability_s2c".asId()
}