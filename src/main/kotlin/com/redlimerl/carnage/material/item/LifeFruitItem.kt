package com.redlimerl.carnage.material.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.Tag
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.*

class LifeFruitItem(settings: Settings?) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack> {
        user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)!!.baseValue += 4f
        val stack = user.getStackInHand(hand)
        if (!user.isCreative) stack.decrement(1)
        user.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1f, 1.5f)
        return TypedActionResult.consume(stack)
    }

    override fun appendTooltip(stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?) {
        tooltip?.add(TranslatableText("item.carnage.life_fruit.tooltip"))
    }

}