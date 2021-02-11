package com.redlimerl.carnage.entity

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.registry.CarnageItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.CreeperEntity
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.explosion.Explosion.DestructionType
import java.util.*

class KillbotEntityData {
    companion object {
        val FUSE_SPEED: TrackedData<Int> = DataTracker.registerData(KillbotEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}

class KillbotEntity(entityType: EntityType<out HostileEntity>?, world: World?) : HostileEntity(entityType, world) {

    private var lastFuseTime = 0
    private var currentFuseTime = 0
    private var fuseTime = 55
    private var explosionRadius = 7

    init {
        experiencePoints = 8
    }

    override fun getAmbientSound(): SoundEvent? {
        return null
    }

    override fun getDeathSound(): SoundEvent? {
        return SoundEvents.ENTITY_WITHER_DEATH
    }

    override fun getHurtSound(source: DamageSource?): SoundEvent? {
        return SoundEvents.ENTITY_WITHER_HURT
    }

    override fun getSoundPitch(): Float {
        return 2.0f
    }

    override fun tickMovement() {
        super.tickMovement()
        if (this.isAlive) {
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder? {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.65)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0)
        }
    }

    override fun initGoals() {
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(2, KillbotIgniteGoal(this))
        goalSelector.add(4, MeleeAttackGoal(this, 1.0, false))
        goalSelector.add(5, WanderAroundFarGoal(this, 0.8))
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(1, FollowTargetGoal<PlayerEntity>(this, PlayerEntity::class.java, true))
        targetSelector.add(2, RevengeGoal(this, CarcanoEntity::class.java, KillbotEntity::class.java, CognitionEntity::class.java))
    }

    fun createCreeperAttributes(): DefaultAttributeContainer.Builder? {
        return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
    }

    override fun getSafeFallDistance(): Int {
        return if (target == null) 3 else 3 + (this.health - 1.0f).toInt()
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, damageSource: DamageSource?): Boolean {
        val bl = super.handleFallDamage(fallDistance, damageMultiplier, damageSource)
        currentFuseTime = (currentFuseTime.toFloat() + fallDistance * 1.5f).toInt()
        if (currentFuseTime > fuseTime - 5) {
            currentFuseTime = fuseTime - 5
        }
        return bl
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(KillbotEntityData.FUSE_SPEED, -1)
    }

    override fun writeCustomDataToTag(tag: CompoundTag) {
        super.writeCustomDataToTag(tag)
        tag.putShort("Fuse", fuseTime.toShort())
        tag.putByte("ExplosionRadius", explosionRadius.toByte())
    }

    override fun readCustomDataFromTag(tag: CompoundTag) {
        super.readCustomDataFromTag(tag)
        if (tag.contains("Fuse", 99)) {
            fuseTime = tag.getShort("Fuse").toInt()
        }
        if (tag.contains("ExplosionRadius", 99)) {
            explosionRadius = tag.getByte("ExplosionRadius").toInt()
        }
    }

    override fun tick() {
        if (this.isAlive) {
            lastFuseTime = currentFuseTime
            val i = getFuseSpeed()
            if (i > 0 && currentFuseTime == 0) {
                playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f)
            }
            currentFuseTime += i
            if (currentFuseTime < 0) {
                currentFuseTime = 0
            }
            if (currentFuseTime >= fuseTime) {
                currentFuseTime = fuseTime
                explode()
            }
        }
        super.tick()
    }

    override fun tryAttack(target: Entity?): Boolean {
        return true
    }

    @Environment(EnvType.CLIENT)
    fun getClientFuseTime(timeDelta: Float): Float {
        return MathHelper.lerp(timeDelta, lastFuseTime.toFloat(), currentFuseTime.toFloat()) / (fuseTime - 2).toFloat()
    }

    fun getFuseSpeed(): Int {
        return dataTracker.get(KillbotEntityData.FUSE_SPEED) as Int
    }

    fun setFuseSpeed(fuseSpeed: Int) {
        dataTracker.set(KillbotEntityData.FUSE_SPEED, fuseSpeed)
    }


    private fun explode() {
        if (!world.isClient) {
            val destructionType = if (world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING)) DestructionType.DESTROY else DestructionType.NONE
            dead = true
            world.createExplosion(this, this.x, this.y, this.z, 7f, destructionType)
            discard()
            spawnEffectsCloud()
        }
    }

    private fun spawnEffectsCloud() {
        val collection = this.statusEffects
        if (!collection.isEmpty()) {
            val areaEffectCloudEntity = AreaEffectCloudEntity(world, this.x, this.y, this.z)
            areaEffectCloudEntity.radius = 5f
            areaEffectCloudEntity.setRadiusOnUse(-0.5f)
            areaEffectCloudEntity.setWaitTime(15)
            areaEffectCloudEntity.duration = areaEffectCloudEntity.duration / 2
            areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.radius / areaEffectCloudEntity.duration.toFloat())
            val var3: Iterator<*> = collection.iterator()
            while (var3.hasNext()) {
                val statusEffectInstance = var3.next() as StatusEffectInstance
                areaEffectCloudEntity.addEffect(StatusEffectInstance(statusEffectInstance))
            }
            world.spawnEntity(areaEffectCloudEntity)
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
            val itemEntity = this.dropItem(CarnageItems.KILLBOT_FRAGMENT)
            itemEntity?.setCovetedItem()
        }
    }
}

class KillbotIgniteGoal(private val creeper: KillbotEntity) : Goal() {

    private var target: LivingEntity? = null
    override fun canStart(): Boolean {
        val livingEntity = creeper.target
        return creeper.getFuseSpeed() > 0 || livingEntity != null && creeper.squaredDistanceTo(livingEntity) < 9.0
    }

    override fun start() {
        creeper.navigation.stop()
        target = creeper.target
    }

    override fun stop() {
        target = null
    }

    override fun tick() {
        if (target == null) {
            creeper.setFuseSpeed(-1)
        } else if (creeper.squaredDistanceTo(target) > 49.0) {
            creeper.setFuseSpeed(-1)
        } else if (!creeper.visibilityCache.canSee(target)) {
            creeper.setFuseSpeed(-1)
        } else {
            creeper.setFuseSpeed(1)
        }
    }

    init {
        controls = EnumSet.of(Control.MOVE)
    }
}
