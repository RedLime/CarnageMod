package com.redlimerl.carnage.material

import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient

class CarnageMaterials : ToolMaterial {

    companion object {
        val INSTANCE: CarnageMaterials = CarnageMaterials()
    }

    override fun getRepairIngredient(): Ingredient {
        return Ingredient.ofItems(CarnageItems.CARNAGE_INGOT)
    }

    override fun getDurability(): Int {
        return 3000
    }

    override fun getAttackDamage(): Float {
        return 0f
    }

    override fun getEnchantability(): Int {
        return 15
    }

    override fun getMiningSpeedMultiplier(): Float {
        return 30f
    }

    override fun getMiningLevel(): Int {
        return 5
    }

}