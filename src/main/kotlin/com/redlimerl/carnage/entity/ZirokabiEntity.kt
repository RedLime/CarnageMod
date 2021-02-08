package com.redlimerl.carnage.entity

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.client.render.entity.PhantomEntityRenderer
import net.minecraft.client.render.entity.model.PhantomEntityModel
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity.createHostileAttributes
import net.minecraft.entity.mob.PhantomEntity
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class ZirokabiEntity(entityType: EntityType<out PhantomEntity>, world: World?) : PhantomEntity(entityType, world) {

    init {
        experiencePoints = 8
    }

    override fun getSoundPitch(): Float {
        return 0.4f+(Math.random().toFloat()*0.3f)
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder? {
            return createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0).add(EntityAttributes.GENERIC_ARMOR, 8.0)
        }
    }

    override fun canSpawn(world: WorldView): Boolean {
        return canSpawn(this.world, SpawnReason.MOB_SUMMONED)
    }


    override fun canSpawn(world: WorldAccess, spawnReason: SpawnReason): Boolean {
        if (spawnReason == SpawnReason.NATURAL && entityWorld.isPlayerInRange(x, y, z, 32.0)) {
            return false
        }

        var d = this.y
        val original = this.y

        while (d > 1) {
            setPosition(this.x, d, this.z)
            if (world.isSpaceEmpty(this) && !world.getBlockState(blockPos.add(0,-1,0)).isAir) {
                return true
            }
            --d
        }

        d = original+1
        while (d < world.topY.toDouble()) {
            setPosition(this.x, d, this.z)
            if (world.isSpaceEmpty(this) && !world.getBlockState(blockPos.add(0,-1,0)).isAir) {
                return true
            }
            ++d
            if (d == world.topY.toDouble()) {
                return false
            }
        }

        return true
    }

    override fun getLimitPerChunk(): Int {
        return (Math.random()*1).toInt()
    }


    override fun dropEquipment(source: DamageSource?, lootingMultiplier: Int, allowDrops: Boolean) {
        if (Math.random() < 0.35) {
            val itemEntity = this.dropItem(CarnageItems.ZIROKABI_MEMBRANE)
            itemEntity?.setCovetedItem()
        }
    }
}