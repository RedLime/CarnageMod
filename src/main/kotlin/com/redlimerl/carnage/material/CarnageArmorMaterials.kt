package com.redlimerl.carnage.material

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents


class CarnageArmorMaterials : ArmorMaterial {

    companion object {
        private val BASE_DURABILITY = intArrayOf(13, 15, 16, 11)
        private val PROTECTION_VALUES = intArrayOf(3, 6, 8, 3)
        val INSTANCE = CarnageArmorMaterials()
    }

    override fun getRepairIngredient(): Ingredient {
        return Ingredient.ofItems(CarnageItems.CARNAGE_INGOT)
    }

    override fun getToughness(): Float {
        return 4.0f
    }

    override fun getKnockbackResistance(): Float {
        return 0.2f
    }

    override fun getEquipSound(): SoundEvent {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE
    }

    override fun getName(): String {
        return "carnage"
    }

    override fun getDurability(slot: EquipmentSlot?): Int {
        return BASE_DURABILITY[slot?.entitySlotId ?: 0] * 3500
    }

    override fun getEnchantability(): Int {
        return 15
    }

    override fun getProtectionAmount(slot: EquipmentSlot?): Int {
        return PROTECTION_VALUES[slot?.entitySlotId ?: 0]
    }
}