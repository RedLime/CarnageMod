package com.redlimerl.carnage.entity

import com.redlimerl.carnage.gui.CarnageCraftingTableScreenHandler
import com.redlimerl.carnage.gui.ImplementedInventory
import com.redlimerl.carnage.registry.CarnageBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

open class CarnageCraftingTableEntity(pos: BlockPos, state: BlockState) : BlockEntity(CarnageBlockEntities.CARNAGE_CRAFTING_TABLE_ENTITY, pos, state), ImplementedInventory, NamedScreenHandlerFactory {

    private val itemList: DefaultedList<ItemStack> = DefaultedList.ofSize(5, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack> {
        return itemList
    }

    override fun markDirty() {

    }

    override fun getDisplayName(): Text {
        return TranslatableText(cachedState.block.translationKey)
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler? {
        return CarnageCraftingTableScreenHandler(syncId, inv, this)
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean {
        return true
    }
}