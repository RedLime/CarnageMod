package com.redlimerl.carnage.material.item

import com.redlimerl.carnage.registry.CarnageItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.BundleTooltipData
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.util.*
import java.util.stream.Stream
import kotlin.math.min

@Suppress("FunctionName")
class ZirokabiBundleItem(settings: Settings) : Item(settings) {

    companion object {
        private val ITEM_BAR_COLOR = MathHelper.packRgb(0.4f, 0.4f, 1.0f)

        @Environment(EnvType.CLIENT)
        fun getAmountFilled(stack: ItemStack): Float {
            return getBundleOccupancy(stack).toFloat() / 128.0f
        }

        private fun addToBundle(bundle: ItemStack, stack: ItemStack): Int {
            if (!stack.isEmpty && stack.item.hasStoredInventory()) {
                val compoundTag = bundle.orCreateTag
                if (!compoundTag.contains("Items")) {
                    compoundTag.put("Items", ListTag())
                }
                val i = getBundleOccupancy(bundle)
                val j = getItemOccupancy(stack)
                val k = stack.count.coerceAtMost((128 - i) / j)
                if (k == 0) {
                    return 0
                } else {
                    val listTag = compoundTag.getList("Items", 10)
                    val optional = method_32344(stack, listTag)
                    if (optional.isPresent) {
                        val compoundTag2 = optional.get()
                        val itemStack = ItemStack.fromTag(compoundTag2)
                        if (itemStack.count + k > 64) {
                            val r = itemStack.count + k - 64
                            itemStack.count = 64
                            itemStack.toTag(compoundTag2)
                            listTag.remove(compoundTag2)
                            listTag.add(0, compoundTag2)

                            val itemStack2 = stack.copy()
                            itemStack2.count = r
                            val compoundTag3 = CompoundTag()
                            itemStack2.toTag(compoundTag3)
                            listTag.add(0, compoundTag3)
                        } else {
                            itemStack.increment(k)
                            itemStack.toTag(compoundTag2)
                            listTag.remove(compoundTag2)
                            listTag.add(0, compoundTag2)
                        }
                    } else {
                        val itemStack2 = stack.copy()
                        itemStack2.count = k
                        val compoundTag3 = CompoundTag()
                        itemStack2.toTag(compoundTag3)
                        listTag.add(0, compoundTag3)
                    }
                    return k
                }
            } else {
                return 0
            }
        }


        private fun method_32344(itemStack: ItemStack, listTag: ListTag): Optional<CompoundTag> {
            return if (itemStack.isOf(CarnageItems.ZIROKABI_BUNDLE)) {
                Optional.empty()
            } else {
                var var10000: Stream<*> = listTag.stream()
                var10000 = var10000.filter { obj -> CompoundTag::class.java.isInstance(obj) }
                var10000.map { obj -> CompoundTag::class.java.cast(obj) }.filter { compoundTag -> ItemStack.canCombine(ItemStack.fromTag(compoundTag), itemStack) }.findFirst()
            }
        }

        private fun getItemOccupancy(stack: ItemStack): Int {
            return if (stack.isOf(CarnageItems.ZIROKABI_BUNDLE)) 4 + getBundleOccupancy(stack) else 64 / stack.maxCount
        }

        private fun getBundleOccupancy(stack: ItemStack): Int {
            return getBundledStacks(stack).mapToInt { itemStack: ItemStack -> getItemOccupancy(itemStack) * itemStack.count }.sum()
        }

        private fun method_32759(itemStack: ItemStack): Optional<ItemStack> {
            val compoundTag = itemStack.orCreateTag
            return if (!compoundTag.contains("Items")) {
                Optional.empty()
            } else {
                val listTag = compoundTag.getList("Items", 10)
                if (listTag.isEmpty()) {
                    Optional.empty()
                } else {
                    val compoundTag2 = listTag.getCompound(0)
                    val itemStack2 = ItemStack.fromTag(compoundTag2)
                    listTag.removeAt(0)
                    if (listTag.isEmpty()) {
                        itemStack.removeSubTag("Items")
                    }
                    Optional.of(itemStack2)
                }
            }
        }

        private fun dropAllBundledItems(stack: ItemStack, player: PlayerEntity): Boolean {
            val compoundTag = stack.orCreateTag
            return if (!compoundTag.contains("Items")) {
                false
            } else {
                if (player is ServerPlayerEntity) {
                    val listTag = compoundTag.getList("Items", 10)
                    for (i in listTag.indices) {
                        val compoundTag2 = listTag.getCompound(i)
                        val itemStack = ItemStack.fromTag(compoundTag2)
                        player.dropItem(itemStack, true)
                    }
                }
                stack.removeSubTag("Items")
                true
            }
        }

        private fun getBundledStacks(stack: ItemStack): Stream<ItemStack> {
            val compoundTag = stack.tag
            return if (compoundTag == null) {
                Stream.empty()
            } else {
                val listTag = compoundTag.getList("Items", 10)
                val var10000: Stream<*> = listTag.stream()
                CompoundTag::class.java.javaClass
                var10000.map { obj -> CompoundTag::class.java.cast(obj) }.map { tag -> ItemStack.fromTag(tag) }
            }
        }
    }

    override fun onStackClicked(stack: ItemStack, slot: Slot, clickType: ClickType, playerInventory: PlayerInventory): Boolean {
        return if (clickType != ClickType.RIGHT) {
            false
        } else {
            val itemStack = slot.stack
            if (itemStack.isEmpty) {
                method_32759(stack).ifPresent { itemStack2: ItemStack? -> addToBundle(stack, slot.method_32756(itemStack2)) }
            } else if (itemStack.item.hasStoredInventory()) {
                val i = (128 - getBundleOccupancy(stack)) / getItemOccupancy(itemStack)
                addToBundle(stack, slot.method_32753(itemStack.count, i, playerInventory.player))
            }
            true
        }
    }

    override fun onClicked(stack: ItemStack, otherStack: ItemStack, slot: Slot, clickType: ClickType, playerInventory: PlayerInventory): Boolean {
        return if (clickType == ClickType.RIGHT && slot.method_32754(playerInventory.player)) {
            if (otherStack.isEmpty) {
                method_32759(stack).ifPresent { s: ItemStack? -> playerInventory.cursorStack = s }
            } else {
                otherStack.decrement(addToBundle(stack, otherStack))
            }
            true
        } else {
            false
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack>? {
        val itemStack = user.getStackInHand(hand)
        return if (dropAllBundledItems(itemStack, user)) {
            user.incrementStat(Stats.USED.getOrCreateStat(this))
            TypedActionResult.success(itemStack, world.isClient())
        } else {
            TypedActionResult.fail(itemStack)
        }
    }

    @Environment(EnvType.CLIENT)
    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return getBundleOccupancy(stack) > 0
    }

    @Environment(EnvType.CLIENT)
    override fun getItemBarStep(stack: ItemStack): Int {
        return min(1 + 12 * getBundleOccupancy(stack) / 128, 13)
    }

    @Environment(EnvType.CLIENT)
    override fun getItemBarColor(stack: ItemStack?): Int {
        return ITEM_BAR_COLOR
    }

    @Environment(EnvType.CLIENT)
    override fun getTooltipData(stack: ItemStack): Optional<TooltipData>? {
        val defaultedList = DefaultedList.of<ItemStack>()
        getBundledStacks(stack).forEach { e: ItemStack -> defaultedList.add(e) }
        return Optional.of(BundleTooltipData(defaultedList, getBundleOccupancy(stack)))
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text?>, context: TooltipContext?) {
        tooltip.add(TranslatableText("item.minecraft.bundle.fullness", getBundleOccupancy(stack), 128).formatted(Formatting.GRAY))
    }

    override fun onItemEntityDestroyed(entity: ItemEntity) {
        ItemUsage.spawnItemContents(entity, getBundledStacks(entity.stack))
    }
}