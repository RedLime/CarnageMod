package com.redlimerl.carnage.mixin;

import com.redlimerl.carnage.game.enchantment.MultiHitEnchantment;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class MultihitMixinJava extends LivingEntity {

    protected MultihitMixinJava(EntityType<LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Redirect(method = {"attack(Lnet/minecraft/entity/Entity;)V"}, at = @At(
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
                    value = "INVOKE"))
    public final boolean multiHitDamage(@NotNull Entity entity, @Nullable DamageSource damageSource, float f) {
        Intrinsics.checkNotNullParameter(entity, "entity");
        ItemStack var10000 = this.getMainHandStack();
        Intrinsics.checkNotNullExpressionValue(var10000, "mainHandStack");
        ListTag listTag = var10000.getEnchantments();
        int i = 0;
        Intrinsics.checkNotNullExpressionValue(listTag, "listTag");

        for(int var6 = ((Collection)listTag).size(); i < var6; ++i) {
            String var9 = listTag.getCompound(i).getString("id");
            Intrinsics.checkNotNullExpressionValue(var9, "listTag.getCompound(i).getString(\"id\")");
            int j = listTag.getCompound(i).getInt("lvl");
            if (Intrinsics.areEqual(var9, MultiHitEnchantment.Companion.getID().getNamespace() + ":" + MultiHitEnchantment.Companion.getID().getPath()) && Random.Default.nextInt(20) < j) {
                entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.5D, 0.0D);
                entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + Random.Default.nextDouble(-1.0D, 1.0D), entity.getY(), entity.getZ() + Random.Default.nextDouble(-1.0D, 1.0D), 0.0D, 0.5D, 0.0D);
                entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + Random.Default.nextDouble(-1.0D, 1.0D), entity.getY(), entity.getZ() + Random.Default.nextDouble(-1.0D, 1.0D), 0.0D, 0.5D, 0.0D);
                entity.world.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + Random.Default.nextDouble(-1.0D, 1.0D), entity.getY(), entity.getZ() + Random.Default.nextDouble(-1.0D, 1.0D), 0.0D, 0.5D, 0.0D);
                return entity.damage(damageSource, f * (float)2);
            }
        }

        return entity.damage(damageSource, f);
    }
}
