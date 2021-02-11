package com.redlimerl.carnage.entity

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.FollowTargetGoal
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.SwimGoal
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.boss.BossBar
import net.minecraft.entity.boss.ServerBossBar
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.VexEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.World
import java.util.*

class TheRuptureEntity(entityType: EntityType<out VexEntity>?, world: World?) : VexEntity(entityType, world) {

    private var originalPos: BlockPos? = null

    constructor(entityType: EntityType<out VexEntity>?, world: World?, op: BlockPos): this(entityType, world) {
        this.originalPos = op
    }

    init {
        experiencePoints = 200
    }


    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder? {
            return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.15)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0).add(EntityAttributes.GENERIC_ARMOR, 10.0)
        }
    }


    private val bossBar = ServerBossBar(this.displayName, BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true).setThickenFog(true) as ServerBossBar

    override fun tick() {
        this.noClip = true
        super.tick()
        this.noClip = false
        setNoGravity(true)
    }


    override fun mobTick() {
        super.mobTick()
        bossBar.percent = this.health / this.maxHealth
    }

    override fun onStartedTrackingBy(player: ServerPlayerEntity?) {
        super.onStartedTrackingBy(player)
        bossBar.addPlayer(player)
    }

    override fun onStoppedTrackingBy(player: ServerPlayerEntity?) {
        super.onStoppedTrackingBy(player)
        bossBar.removePlayer(player)
    }

    override fun initGoals() {
        goalSelector.add(0, SwimGoal(this))
        goalSelector.add(4, ChargeTargetGoal())
        goalSelector.add(8, LookAtTargetGoal())
        goalSelector.add(9, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f, 1.0f))
        goalSelector.add(10, LookAtEntityGoal(this, MobEntity::class.java, 8.0f))
        targetSelector.add(1, FollowTargetGoal<PlayerEntity>(this, PlayerEntity::class.java, 0, false, false, null))
    }

    override fun initEquipment(difficulty: LocalDifficulty?) {
        equipStack(EquipmentSlot.MAINHAND, ItemStack(CarnageItems.CARNAGE_SWORD))
        setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f)
    }

    override fun cannotDespawn(): Boolean {
        return true
    }

    private fun getOriginalBlockPos(): BlockPos {
        return originalPos ?: blockPos
    }

    override fun readCustomDataFromTag(tag: CompoundTag?) {
        if (tag != null && tag.contains("PosX")) {
            originalPos = BlockPos(tag.getInt("PosX"), tag.getInt("PosY"), tag.getInt("PosZ"))
        }
        super.readCustomDataFromTag(tag)
    }

    override fun writeCustomDataToTag(tag: CompoundTag?) {
        if (tag != null && originalPos != null) {
            tag.putInt("PosX", originalPos!!.x)
            tag.putInt("PosY", originalPos!!.y)
            tag.putInt("PosZ", originalPos!!.z)
        }
    }

    inner class LookAtTargetGoal : Goal() {
        override fun canStart(): Boolean {
            return !this@TheRuptureEntity.getMoveControl().isMoving && this@TheRuptureEntity.random.nextInt(5) == 0
        }

        override fun shouldContinue(): Boolean {
            return false
        }

        override fun tick() {
            for (i in 0..2) {
                val blockPos2: BlockPos = getOriginalBlockPos().add(this@TheRuptureEntity.random.nextInt(30) - 15, this@TheRuptureEntity.random.nextInt(11) - 5, this@TheRuptureEntity.random.nextInt(30) - 15)
                if (this@TheRuptureEntity.world.isAir(blockPos2)) {
                    this@TheRuptureEntity.moveControl.moveTo(blockPos2.x.toDouble() + 0.5, blockPos2.y.toDouble() + 0.5, blockPos2.z.toDouble() + 0.5, 1.2)
                    if (this@TheRuptureEntity.target == null) {
                        this@TheRuptureEntity.getLookControl().lookAt(blockPos2.x.toDouble() + 0.5, blockPos2.y.toDouble() + 0.5, blockPos2.z.toDouble() + 0.5, 180.0f, 20.0f)
                    }
                    break
                }
            }
        }

        init {
            controls = EnumSet.of(Control.MOVE)
        }
    }

    inner class ChargeTargetGoal : Goal() {
        override fun canStart(): Boolean {
            return if (this@TheRuptureEntity.target != null && !this@TheRuptureEntity.getMoveControl().isMoving && this@TheRuptureEntity.random.nextInt(5) == 0) {
                this@TheRuptureEntity.squaredDistanceTo(this@TheRuptureEntity.target) > 2.0
            } else {
                false
            }
        }

        override fun shouldContinue(): Boolean {
            return this@TheRuptureEntity.getMoveControl().isMoving && this@TheRuptureEntity.isCharging && this@TheRuptureEntity.target != null && this@TheRuptureEntity.target!!.isAlive
        }

        override fun start() {
            val livingEntity: LivingEntity = this@TheRuptureEntity.target!!
            val vec3d = livingEntity.getCameraPosVec(1.0f)
            this@TheRuptureEntity.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.5)
            this@TheRuptureEntity.isCharging = true
            this@TheRuptureEntity.playSound(SoundEvents.ENTITY_GHAST_WARN, 1.0f, 1.5f)
        }

        override fun stop() {
            this@TheRuptureEntity.isCharging = false
        }

        override fun tick() {
            val livingEntity: LivingEntity = this@TheRuptureEntity.target!!
            if (this@TheRuptureEntity.squaredDistanceTo(livingEntity) < 4) {
                this@TheRuptureEntity.tryAttack(livingEntity)
                this@TheRuptureEntity.isCharging = false
            } else {
                val vec3d = livingEntity.getCameraPosVec(1.0f)
                this@TheRuptureEntity.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.5)
            }
        }

        init {
            controls = EnumSet.of(Control.MOVE)
        }
    }

    override fun dropEquipment(source: DamageSource?, lootingMultiplier: Int, allowDrops: Boolean) {
        this.dropItem(CarnageItems.LIFE_FRUIT)?.setCovetedItem()
        this.dropItem(CarnageItems.REDLIME_INGOT)?.setCovetedItem()
    }
}