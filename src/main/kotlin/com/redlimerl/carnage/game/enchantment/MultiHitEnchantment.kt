package com.redlimerl.carnage.game.enchantment

import com.redlimerl.carnage.CarnageMod
import net.minecraft.enchantment.DamageEnchantment
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import kotlin.random.Random

class MultiHitEnchantment : Enchantment(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, arrayOf(EquipmentSlot.MAINHAND)) {

    companion object {
        val ID = CarnageMod.identifier("multihit")
    }

    override fun getMinPower(level: Int): Int {
        return level * 10
    }

    override fun getMaxPower(level: Int): Int {
        return getMinPower(level) + 15
    }

    override fun getMaxLevel(): Int {
        return 4
    }
}