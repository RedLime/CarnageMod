package com.redlimerl.carnage.registry

import com.redlimerl.carnage.game.enchantment.MultiHitEnchantment
import net.minecraft.enchantment.Enchantment
import net.minecraft.util.registry.Registry


class CarnageEnchantments {

    companion object {
        val MULTIHIT: Enchantment = MultiHitEnchantment()

        fun registry() {
            Registry.register(Registry.ENCHANTMENT, MultiHitEnchantment.ID, MULTIHIT)
        }
    }
}