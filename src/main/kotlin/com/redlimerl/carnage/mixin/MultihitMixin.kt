package com.redlimerl.carnage.mixin

import com.redlimerl.carnage.game.enchantment.MultiHitEnchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import kotlin.random.Random

@Mixin(PlayerEntity::class)
abstract class MultihitMixin protected constructor(entityType_1: EntityType<PlayerEntity>?, world_1: World?) : LivingEntity(entityType_1, world_1) {

    @Redirect(method = ["attack(Lnet/minecraft/entity/Entity;)V"], at = At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    fun multiHitDamage(entity: Entity, damageSource: DamageSource?, f: Float): Boolean {

        val listTag = mainHandStack.enchantments

        for (i in listTag.indices) {
            val string: String = listTag.getCompound(i).getString("id")
            val j: Int = listTag.getCompound(i).getInt("lvl")
            if (string == MultiHitEnchantment.ID.namespace+":"+MultiHitEnchantment.ID.path) {
                if (Random.nextInt(20) < j) {
                    entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.x, entity.y, entity.z, 0.0, 0.5, 0.0)
                    entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.x + (Random.nextDouble(-1.0, 1.0)), entity.y, entity.z + (Random.nextDouble(-1.0, 1.0)), 0.0, 0.5, 0.0)
                    entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.x + (Random.nextDouble(-1.0, 1.0)), entity.y, entity.z + (Random.nextDouble(-1.0, 1.0)), 0.0, 0.5, 0.0)
                    entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.x + (Random.nextDouble(-1.0, 1.0)), entity.y, entity.z + (Random.nextDouble(-1.0, 1.0)), 0.0, 0.5, 0.0)
                    return entity.damage(damageSource, f*2)
                }
            }
        }
        return entity.damage(damageSource, f)
    }
}