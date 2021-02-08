@file:Suppress("MemberVisibilityCanBePrivate")

package com.redlimerl.carnage.registry

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.material.block.CarnageCraftingTable
import com.redlimerl.carnage.material.block.CarnageFacingBlock
import com.redlimerl.carnage.material.block.CarnageStairsBlock
import com.redlimerl.carnage.material.block.TheRuptureSpawnBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

class CarnageBlocks {

    companion object {
        val CARNAGE_BLOCK = Block(FabricBlockSettings.of(Material.METAL).strength(25f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.METAL))
        val CARNAGE_ORE = Block(FabricBlockSettings.of(Material.STONE).strength(20f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CARNAGE_TABLE = CarnageCraftingTable(FabricBlockSettings.of(Material.METAL).strength(6.5f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.METAL))

        val CADREGA_PORTAL = TheRuptureSpawnBlock(FabricBlockSettings.of(Material.STONE).strength(30f).breakByTool(FabricToolTags.PICKAXES, 5).requiresTool().sounds(BlockSoundGroup.STONE))

        val CADREGA_STONE = Block(FabricBlockSettings.of(Material.STONE).hardness(3.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_STONE_WALL = WallBlock(FabricBlockSettings.of(Material.STONE).hardness(6f).resistance(9f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_PATCH_STONE = Block(FabricBlockSettings.of(Material.STONE).hardness(5.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_STONE_BRICK = CarnageFacingBlock(FabricBlockSettings.of(Material.STONE).hardness(5.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_STONE_BRICK_STAIRS = CarnageStairsBlock(CADREGA_STONE_BRICK.defaultState, FabricBlockSettings.of(Material.STONE).hardness(5.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_STONE_BRICK_SLAB = SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(5.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))
        val CADREGA_STONE_CHISELED = Block(FabricBlockSettings.of(Material.STONE).hardness(5.5f).resistance(6f).breakByTool(FabricToolTags.PICKAXES, 3).requiresTool().sounds(BlockSoundGroup.STONE))

        fun registry() {
            Registry.register(Registry.BLOCK, CarnageMod.identifier("carnage_block"), CARNAGE_BLOCK)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_block"), BlockItem(CARNAGE_BLOCK, Item.Settings().group(CarnageGroups.CARNAGE_TAB)))
            Registry.register(Registry.BLOCK, CarnageMod.identifier("carnage_ore"), CARNAGE_ORE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_ore"), BlockItem(CARNAGE_ORE, Item.Settings().group(CarnageGroups.CARNAGE_TAB)))
            registerBlock("carnage_crafting_table", CARNAGE_TABLE)
            registerBlock("cadrega_stone", CADREGA_STONE)
            registerBlock("cadrega_stone_wall", CADREGA_STONE_WALL)
            registerBlock("cadrega_patch_stone", CADREGA_PATCH_STONE)
            registerBlock("cadrega_stone_brick", CADREGA_STONE_BRICK)
            registerBlock("cadrega_stone_brick_stairs", CADREGA_STONE_BRICK_STAIRS)
            registerBlock("cadrega_stone_brick_slab", CADREGA_STONE_BRICK_SLAB)
            registerBlock("chiseled_cadrega_stone_brick", CADREGA_STONE_CHISELED)
            registerBlock("cadrega_portal", CADREGA_PORTAL)
        }

        private fun registerBlock(s: String, b: Block) {
            Registry.register(Registry.BLOCK, CarnageMod.identifier(s), b)
            Registry.register(Registry.ITEM, CarnageMod.identifier(s), BlockItem(b, Item.Settings().group(CarnageGroups.CARNAGE_TAB)))
        }
    }
}