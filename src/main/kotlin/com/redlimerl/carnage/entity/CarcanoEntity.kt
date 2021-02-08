package com.redlimerl.carnage.entity

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class CarcanoEntity(entityType: EntityType<out HostileEntity>?, world: World?) : HostileEntity(entityType, world) {

    init {
        experiencePoints = 8
    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH
    }

    override fun getHurtSound(source: DamageSource?): SoundEvent? {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT
    }

    override fun getSoundPitch(): Float {
        return 0.4f+(Math.random().toFloat()*0.3f)
    }

    override fun playStepSound(pos: BlockPos?, state: BlockState?) {
        playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP, 0.15f, 1.0f)
        super.playStepSound(pos, state)
    }

    override fun getLootTableId(): Identifier {
        return CarnageMod.identifier("entities/carcano")
    }

    override fun initGoals() {
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(1, MeleeAttackGoal(this, 0.75, false))
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))
        targetSelector.add(0, RevengeGoal(this, CarcanoEntity::class.java, KillbotEntity::class.java, CognitionEntity::class.java))
        targetSelector.add(1, FollowTargetGoal<PlayerEntity>(this, PlayerEntity::class.java, true))
        targetSelector.add(2, FollowTargetGoal<IronGolemEntity>(this, IronGolemEntity::class.java, true))
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder? {
            return createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0).add(EntityAttributes.GENERIC_ARMOR, 8.0)
                    .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.0)
        }
    }

    override fun tryAttack(target: Entity?): Boolean {
        val bl = super.tryAttack(target)
        if (bl) {
            val f = world.getLocalDifficulty(blockPos).localDifficulty
            target!!.setOnFireFor(2 * f.toInt())
        }
        return bl
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
            val itemEntity = this.dropItem(CarnageItems.CARCANO_FRAGMENT)
            itemEntity?.setCovetedItem()
        }
    }
}