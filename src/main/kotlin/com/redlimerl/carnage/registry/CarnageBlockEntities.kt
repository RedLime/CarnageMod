package com.redlimerl.carnage.registry

import com.redlimerl.carnage.CarnageMod.Companion.identifier
import com.redlimerl.carnage.entity.CarnageCraftingTableEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import kotlin.reflect.KFunction2

class CarnageBlockEntities {

    companion object {
        val CARNAGE_CRAFTING_TABLE_ENTITY: BlockEntityType<CarnageCraftingTableEntity> =
            Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier("carnage_crafting_table"), build(::CarnageCraftingTableEntity, CarnageBlocks.CARNAGE_TABLE))


        fun registry() {

        }

        private fun build(factory: KFunction2<@ParameterName(name = "pos") BlockPos, @ParameterName(name = "state") BlockState, CarnageCraftingTableEntity>, vararg blocks: Block): BlockEntityType<CarnageCraftingTableEntity> {
            return BlockEntityType.Builder.create(factory::invoke, blocks).build(null)
        }
    }
}