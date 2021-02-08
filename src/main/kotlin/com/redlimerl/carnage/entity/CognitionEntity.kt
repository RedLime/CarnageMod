package com.redlimerl.carnage.entity

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.entity.*
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil.createArrowProjectile
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.world.*

class CognitionEntity(entityType: EntityType<out HostileEntity>?, world: World?) : HostileEntity(entityType, world), RangedAttackMob {

    private val bowAttackGoal = BowAttackGoal(this, 1.0, 15,30.0f)

    init {
        updateAttackType()
        equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
        experiencePoints = 8
    }

    override fun getAmbientSound(): SoundEvent? {
        return SoundEvents.ENTITY_STRAY_AMBIENT
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_STRAY_DEATH
    }

    override fun getHurtSound(source: DamageSource?): SoundEvent? {
        return SoundEvents.ENTITY_STRAY_HURT
    }

    override fun getSoundPitch(): Float {
        return 1.25f+(Math.random().toFloat()*0.3f)
    }

    override fun attack(target: LivingEntity, pullProgress: Float) {
        val itemStack = ItemStack(Items.ARROW)
        val persistentProjectileEntity = createArrowProjectile(this, itemStack, pullProgress)
        val d = target.x - this.x
        val e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.y
        val f = target.z - this.z
        val g = MathHelper.sqrt(d * d + f * f).toDouble()
        persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, 1.6f, (14 - world.difficulty.id * 4).toFloat()*2)
        persistentProjectileEntity.damage = persistentProjectileEntity.damage * 3
        playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (getRandom().nextFloat() * 0.4f + 0.8f))
        world.spawnEntity(persistentProjectileEntity)
    }

    override fun initGoals() {
        goalSelector.add(5, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(1, RevengeGoal(this, CarcanoEntity::class.java, KillbotEntity::class.java, CognitionEntity::class.java))
        targetSelector.add(2, FollowTargetGoal<PlayerEntity>(this, PlayerEntity::class.java, true))
        targetSelector.add(3, FollowTargetGoal<IronGolemEntity>(this, IronGolemEntity::class.java, true))
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder? {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 45.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0)
        }
    }

    override fun getActiveEyeHeight(pose: EntityPose?, dimensions: EntityDimensions?): Float {
        return 1.74f
    }

    override fun getHeightOffset(): Double {
        return -0.6
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

    override fun initEquipment(difficulty: LocalDifficulty?) {
        super.initEquipment(difficulty)
        equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
    }

    override fun initialize(world: ServerWorldAccess?, difficulty: LocalDifficulty?, spawnReason: SpawnReason?, entityData: EntityData?, entityTag: CompoundTag?): EntityData? {
        equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BOW))
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag)
    }

    override fun dropEquipment(source: DamageSource?, lootingMultiplier: Int, allowDrops: Boolean) {
        if (Math.random() < 0.35) {
            val itemEntity = this.dropItem(CarnageItems.COGNITION_FRAGMENT)
            itemEntity?.setCovetedItem()
        }
    }


    fun updateAttackType() {
        if (world != null && !world.isClient) {
            goalSelector.remove(bowAttackGoal)
            goalSelector.add(4, bowAttackGoal)
        }
    }


    override fun readCustomDataFromTag(tag: CompoundTag?) {
        super.readCustomDataFromTag(tag)
        updateAttackType()
    }

    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
        super.equipStack(slot, stack)
        if (!world.isClient) {
            updateAttackType()
        }
    }

}