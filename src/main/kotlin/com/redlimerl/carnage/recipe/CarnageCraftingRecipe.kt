package com.redlimerl.carnage.recipe

import com.redlimerl.carnage.registry.CarnageBlocks
import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class CarnageCraftingRecipe(val result: Item, val mainRecipe: Item, vararg another: Item) {
    private val another = another.toList()

    companion object {
        private val recipes = listOf(
                CarnageCraftingRecipe(CarnageItems.CARNAGE_INGOT, Items.COPPER_INGOT, Items.NETHERITE_SCRAP, CarnageItems.END_SHARD, CarnageItems.CARNAGE_SHARD),
                CarnageCraftingRecipe(CarnageItems.CADREGA_TOTEM, Items.TOTEM_OF_UNDYING, CarnageItems.SHARD_OF_TARTARUS, CarnageItems.SHARD_OF_TARTARUS, CarnageItems.SHARD_OF_TARTARUS),
                CarnageCraftingRecipe(CarnageItems.KEY_OF_CADREGA, Items.ENDER_EYE, Items.REDSTONE_BLOCK, CarnageItems.CARNAGE_SHARD, CarnageItems.CARNAGE_SHARD),
                CarnageCraftingRecipe(CarnageBlocks.CADREGA_PORTAL.asItem(), Items.CRYING_OBSIDIAN, Items.END_STONE, Items.PRISMARINE, Items.NETHERRACK),
                CarnageCraftingRecipe(CarnageItems.TARTARUS, CarnageItems.SHARD_OF_TARTARUS, CarnageItems.CARCANO_FRAGMENT, CarnageItems.COGNITION_FRAGMENT, CarnageItems.KILLBOT_FRAGMENT)
        )

        fun foundRecipeResult(inv: Inventory): ItemStack {
            val main = inv.getStack(1).item
            val recipe = listOf(inv.getStack(2).item,inv.getStack(3).item,inv.getStack(4).item)
            return recipes.find { it.mainRecipe == main &&
                    (recipe.sortedBy { item -> item.translationKey } == it.another.toList().sortedBy { item -> item.translationKey }) }
                    ?.result?.defaultStack ?: ItemStack.EMPTY
        }
    }

    override fun toString(): String {
        return "{CarnageCraftingRecipe:{${result}, ${mainRecipe}, recipes=[${another.joinToString(", ")}]}}"
    }
}