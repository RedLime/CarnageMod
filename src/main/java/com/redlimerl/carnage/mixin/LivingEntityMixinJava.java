package com.redlimerl.carnage.mixin;

import com.redlimerl.carnage.registry.CarnageItems;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixinJava extends Entity {

    @Shadow
    @NotNull
    public final native ItemStack getStackInHand(Hand var1);

    @Shadow
    public final native boolean hasStatusEffect(StatusEffect var1);

    @Shadow
    public final native void setHealth(float var1);

    @Shadow
    public final native boolean clearStatusEffects();

    @Shadow
    public final native boolean addStatusEffect(StatusEffectInstance var1);


    protected LivingEntityMixinJava(EntityType<Entity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(at = {@At("HEAD")}, method = {"tryUseTotem"}, cancellable = true)
    public final boolean useTeleportingTotem(@NotNull DamageSource damageSource_1, CallbackInfoReturnable<Boolean> callback) {
        Intrinsics.checkNotNullParameter(damageSource_1, "damageSource_1");
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (damageSource_1.isOutOfWorld()) {
            return false;
        } else {
            ItemStack itemStack = null;
            Hand[] var4 = Hand.values();
            int var5 = var4.length;
            int var6 = 0;

            for(int var7 = var5; var6 < var7; ++var6) {
                Hand hand = var4[var6];
                ItemStack itemStack2 = this.getStackInHand(hand);
                if (itemStack2.getItem() == CarnageItems.Companion.getCADREGA_TOTEM()) {
                    itemStack = itemStack2.copy();
                    itemStack2.decrement(1);
                    break;
                }
            }

            if (itemStack != null) {
                this.setHealth(2.0F);
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 2));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 900, 3));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                this.world.sendEntityStatus(this, (byte)35);
                callback.setReturnValue(true);
            }

            return itemStack != null;
        }
    }
}
