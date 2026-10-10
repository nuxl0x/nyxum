package nux.nyxum.power

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import nux.nyxum.Nyxum.asId
import nux.nyxum.NyxumAPI
import nux.nyxum.ability.Ability
import nux.nyxum.ability.ClientAbility
import nux.nyxum.ability.ClientActiveAbility
import nux.nyxum.condition.KeyPressed
import nux.nyxum.network.Packets

object PowerSync {
    fun ServerPlayer.syncPower() {
        val power = NyxumAPI.getPower(this) ?: Power()
        val buf = PacketByteBufs.create()
        buf.writePower(power)
        ServerPlayNetworking.send(this, Packets.SYNC_POWER_S2C, buf)
    }

    // WRITING
    fun FriendlyByteBuf.writePower(power: Power) {
        this.writeUtf(power.name)
        this.writeUtf(power.description)
        this.writeResourceLocation(power.icon)

        this.writeInt(power.abilityIds.size)
        power.abilityIds.forEach {
            this.writeResourceLocation(it)
        }

        this.writeInt(power.resources.size)
        power.resources.forEach { (name, resource) ->
            this.writeUtf(name)
            this.writeResource(resource)
        }

        this.writeBoolean(power.shouldDisplayPowerUI)

        this.writeInt(power.abilities.size)
        power.abilities.forEach {
            this.writeAbility(it)
        }

        val activeAbilities = power.abilities.filter { it.conditions.containsKey("key_pressed".asId()) }
        this.writeInt(activeAbilities.size)
        activeAbilities.forEach {
            this.writeActiveAbility(it)
        }

    }

    fun FriendlyByteBuf.writeAbility(ability: Ability) {
        this.writeUtf(ability.name)
        this.writeUtf(ability.description)
        this.writeResourceLocation(ability.icon)
        this.writeResourceLocation(ability.typeId)
    }

    fun FriendlyByteBuf.writeActiveAbility(ability: Ability) {
        this.writeUtf(ability.name)
        this.writeUtf(ability.description)
        this.writeResourceLocation(ability.icon)
        this.writeResourceLocation(ability.typeId)
        val castedConditions = ability.conditions["key_pressed".asId()] as KeyPressed
        this.writeUtf(castedConditions.key)
        this.writeInt(castedConditions.cooldown)
    }

    fun FriendlyByteBuf.writeResource(resource: Resource) {
        this.writeInt(resource.minValue)
        this.writeInt(resource.maxValue)
        this.writeInt(resource.value)
        this.writeBoolean(resource.shouldRender)
    }



    // READING
    fun FriendlyByteBuf.readPower(): ClientPower {
        val name = this.readUtf()
        val description = this.readUtf()
        val icon = this.readResourceLocation()

        val abilityIdsSize = this.readInt()
        val abilityIds = mutableListOf<ResourceLocation>()
        repeat(abilityIdsSize) {
            abilityIds.add(this.readResourceLocation())
        }

        val resourcesSize = this.readInt()
        val resources = mutableMapOf<String, Resource>()
        repeat(resourcesSize) {
            val name = this.readUtf()
            val resource = this.readResource()
            resources[name] = resource
        }

        val shouldDisplayPowerUI = this.readBoolean()

        val abilitiesSize = this.readInt()
        val abilities = mutableListOf<ClientAbility>()
        repeat(abilitiesSize) {
            abilities.add(this.readAbility())
        }

        val activeAbilitiesSize = this.readInt()
        val activeAbilities = mutableListOf<ClientActiveAbility>()
        repeat(activeAbilitiesSize) {
            activeAbilities.add(this.readActiveAbility())
        }

        return ClientPower(
            name,
            description,
            icon,
            abilityIds,
            resources,
            shouldDisplayPowerUI,
            abilities,
            activeAbilities
        )

    }

    fun FriendlyByteBuf.readAbility(): ClientAbility {
        val name = this.readUtf()
        val description = this.readUtf()
        val icon = this.readResourceLocation()
        val typeId = this.readResourceLocation()

        return ClientAbility(
            name,
            description,
            icon,
            typeId
        )
    }

    fun FriendlyByteBuf.readActiveAbility(): ClientActiveAbility {
        val name = this.readUtf()
        val description = this.readUtf()
        val icon = this.readResourceLocation()
        val typeId = this.readResourceLocation()
        val key = this.readUtf()
        val cooldown = this.readInt()

        return ClientActiveAbility(
            name,
            description,
            icon,
            typeId,
            key,
            cooldown
        )
    }

    fun FriendlyByteBuf.readResource(): Resource {
        val minValue = this.readInt()
        val maxValue = this.readInt()
        val value = this.readInt()
        val shouldRender = this.readBoolean()

        return Resource(
            minValue,
            maxValue,
            value,
            shouldRender
        )
    }
}