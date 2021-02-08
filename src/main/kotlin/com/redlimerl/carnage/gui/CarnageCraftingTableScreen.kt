@file:Suppress("DEPRECATION")

package com.redlimerl.carnage.gui

import com.mojang.blaze3d.systems.RenderSystem
import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.recipe.CarnageCraftingRecipe
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World


class CarnageCraftingTableScreenHandler(var invSyncId: Int, private val playerInventory: PlayerInventory, private val inventory: Inventory = SimpleInventory(5)) : ScreenHandler(CarnageMod.CCT_SCREEN_HANDLER_TYPE, invSyncId) {


    init {
        inventory.onOpen(playerInventory.player)

        //Our inventory
        addSlot(ResultSlot(inventory, 0, 135, 33).apply { })
        addSlot(Slot(inventory, 1, 63, 33))

        addSlot(Slot(inventory, 2, 63, 2))
        addSlot(Slot(inventory, 3, 26, 59))
        addSlot(Slot(inventory, 4, 100, 59))

        //The player inventory
        var l: Int
        var m = 0
        while (m < 3) {
            l = 0
            while (l < 9) {
                addSlot(Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 101 + m * 18)) // 84
                ++l
            }
            ++m
        }

        //The player Hotbar
        m = 0
        while (m < 9) {
            addSlot(Slot(playerInventory, m, 8 + m * 18, 159)) //142
            ++m
        }
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }

    // Shift + Player Inv Slot
    override fun transferSlot(player: PlayerEntity, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = slots[index]
        if (slot.hasStack()) {
            val itemStack2 = slot.stack
            itemStack = itemStack2.copy()
            if (index < 5) {
                if (!insertItem(itemStack2, 5, 41, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(itemStack2, 1, 6, false)) {
                return ItemStack.EMPTY
            }
            if (itemStack2.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
            if (itemStack2.count == itemStack.count) {
                return ItemStack.EMPTY
            }
            slot.onTakeItem(player, itemStack2)
        }

        return itemStack
    }

    override fun close(player: PlayerEntity) {
        this.inventory.setStack(0, ItemStack.EMPTY)
        this.dropInventory(player, this.inventory)
    }

    override fun sendContentUpdates() {
        updateResult(invSyncId, playerInventory.player.world, playerInventory.player)
        super.sendContentUpdates()
    }

    fun updateResult(syncId: Int, world: World, player: PlayerEntity) {
        if (!world.isClient) {
            val result = CarnageCraftingRecipe.foundRecipeResult(this.inventory)
            this.inventory.setStack(0, result)

            if (player is ServerPlayerEntity){
                val serverPlayer: ServerPlayerEntity = player
                serverPlayer.networkHandler.sendPacket(ScreenHandlerSlotUpdateS2CPacket(syncId, 0, result))
            }
        }
    }

    override fun onSlotClick(i: Int, j: Int, actionType: SlotActionType, player: PlayerEntity): ItemStack {
        if (i == 0 && getSlot(i).stack != ItemStack.EMPTY) {
            if (SlotActionType.QUICK_CRAFT == actionType || SlotActionType.QUICK_MOVE == actionType) {
                var counts = 0
                while (inventory.getStack(1).count > 0 && inventory.getStack(2).count > 0 && inventory.getStack(3).count > 0 && inventory.getStack(4).count > 0) {
                    inventory.getStack(1).decrement(1)
                    inventory.getStack(2).decrement(1)
                    inventory.getStack(3).decrement(1)
                    inventory.getStack(4).decrement(1)
                    counts++
                }
                inventory.getStack(0).count = counts
            } else if (SlotActionType.CLONE != actionType) {
                inventory.getStack(1).decrement(1)
                inventory.getStack(2).decrement(1)
                inventory.getStack(3).decrement(1)
                inventory.getStack(4).decrement(1)
            }
        }
        if (actionType == SlotActionType.THROW) {
            return super.onSlotClick(i, j, SlotActionType.PICKUP, player)
        }
        return super.onSlotClick(i, j, actionType, player)
    }
}

class CarnageCraftingTableScreen(handler: ScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<ScreenHandler?>(handler, inventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client!!.textureManager.bindTexture(TEXTURE)
        val x = (width - backgroundWidth) / 2
        val y = ((height - backgroundHeight - 35) / 2)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight + 35)
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    override fun init() {
        super.init()
        // Center the title
        titleX = ((backgroundWidth - textRenderer.getWidth(title)) / 2)
        titleY -= 35/2
        playerInventoryTitleY += 35/2
    }

    companion object {
        //A path to the gui texture. In this example we use the texture from the dispenser
        private val TEXTURE: Identifier = CarnageMod.identifier("textures/gui/carnage_crafting.png")
    }
}