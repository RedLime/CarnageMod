package com.redlimerl.carnage.mixin

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable


@Mixin(LivingEntity::class)
abstract class LivingEntityMixin protected constructor(entityType_1: EntityType<*>?, world_1: World?) : Entity(entityType_1, world_1) {

    @Shadow
    external fun getStackInHand(hand_1: Hand?): ItemStack

    @Shadow
    external fun hasStatusEffect(effect: StatusEffect?): Boolean

    @Shadow
    external fun setHealth(health: Float)

    @Shadow
    external fun clearStatusEffects(): Boolean

    @Shadow
    external fun addStatusEffect(statusEffectInstance_1: StatusEffectInstance?): Boolean


    @Inject(at = [At("HEAD")], method = ["tryUseTotem"], cancellable = true)
    fun useTeleportingTotem(damageSource_1: DamageSource, callback: CallbackInfoReturnable<Boolean?>): Boolean {
        if (damageSource_1.isOutOfWorld) {
            return false
        } else {
            var itemStack: ItemStack? = null
            val var4 = Hand.values()
            val var5 = var4.size
            for (var6 in 0 until var5) {
                val hand = var4[var6]
                val itemStack2 = getStackInHand(hand)
                if (itemStack2.item === CarnageItems.CADREGA_TOTEM) {
                    itemStack = itemStack2.copy()
                    itemStack2.decrement(1)
                    break
                }
            }
            if (itemStack != null) {
                if (this is ServerPlayerEntity) {
                    val serverPlayerEntity = this
                    Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack)
                }
                setHealth(2.0f)
                clearStatusEffects()
                addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 900, 2))
                addStatusEffect(StatusEffectInstance(StatusEffects.ABSORPTION, 900, 3))
                addStatusEffect(StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0))
                world.sendEntityStatus(this, 35.toByte())
                callback.returnValue = true
            }
            return itemStack != null
        }
    }
}