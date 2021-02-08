package com.redlimerl.carnage.structure.feature

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.registry.CarnageBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*
import kotlin.math.abs


class BedrockColumnFeature : Feature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {

    companion object {
        val ID = CarnageMod.identifier("bedrock_column")
    }

    private fun getRandomBlock(): BlockState? {
        val random = Random()
        return when (random.nextInt(3)) {
            0 -> CarnageBlocks.CADREGA_STONE.defaultState
            1 -> CarnageBlocks.CADREGA_STONE_BRICK.defaultState
            else -> CarnageBlocks.CADREGA_PATCH_STONE.defaultState
        }
    }

    private fun getBaseHeight(world: ServerWorldAccess, x: Int, y: Int): Int {
        return world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, y) - 1
    }

    private fun placeBottom(world: ServerWorldAccess, pos: BlockPos) {
        world.setBlockState(pos, getRandomBlock(), 3)
    }

    private fun placeSingleWall(world: ServerWorldAccess, pos: BlockPos, isOutsideWall: Boolean) {
        var height = 3
        if (isOutsideWall) {
            val random = Random()
            val heightToAdd = random.nextInt(6)
            height += heightToAdd
        } else {
            height = 2
        }
        for (i in 0 until height) {
            if (world.getBlockState(pos.up(i)).isAir) {
                break
            }
            world.setBlockState(pos.up(i + 1), Blocks.BEDROCK.defaultState, 3)
        }
    }

    private fun fixYForWall(world: ServerWorldAccess, pos: BlockPos): BlockPos {
        val y = getBaseHeight(world, pos.x, pos.z)
        return BlockPos(pos.x, y, pos.z)
    }

    private fun placeBase(random: Random, world: ServerWorldAccess, pos: BlockPos) {
        val i = pos.x
        val j = pos.z
        val fs = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.9f, 0.9f, 0.8f, 0.7f, 0.6f, 0.4f, 0.2f)
        val k = fs.size
        val m = random.nextInt(1.coerceAtLeast(8 - 16 / 2))
        val mutable = BlockPos.ORIGIN.mutableCopy()
        for (o in i - k..i + k) {
            for (p in j - k..j + k) {
                val q = abs(o - i) + abs(p - j)
                val r = 0.coerceAtLeast(q + m)
                if (r < k) {
                    val f = fs[r]
                    if (random.nextDouble() < f.toDouble()) {
                        val t = getBaseHeight(world, o, p)
                        mutable[o, t] = p
                        placeBottom(world, mutable)
                    }
                }
            }
        }
    }

    override fun generate(featureContext: FeatureContext<DefaultFeatureConfig>?): Boolean {
        val random = featureContext?.random ?: return false
        val blockPos = featureContext.pos ?: return false
        val world = featureContext.world ?: return false

        var pos = blockPos
        placeBase(random, world, pos)

        //Generates
        val originalPos = pos
        pos = pos.south(6)
        pos = BlockPos(pos.x, getBaseHeight(world, pos.x, pos.z), pos.z)

        if (getBaseHeight(world, pos.x, pos.z) < 5) {
            return false
        }
        placeSingleWall(world, pos, true)
        placeSingleWall(world, pos.east(), true)
        placeSingleWall(world, pos.west(), true)

        pos = originalPos
        pos = pos.north(6)
        pos = BlockPos(pos.x, getBaseHeight(world, pos.x, pos.z), pos.z)
        placeSingleWall(world, pos, true)
        placeSingleWall(world, pos.east(), true)
        placeSingleWall(world, pos.west(), true)

        pos = originalPos
        pos = pos.west(6)
        pos = BlockPos(pos.x, getBaseHeight(world, pos.x, pos.z), pos.z)
        placeSingleWall(world, pos, true)
        placeSingleWall(world, pos.north(), true)
        placeSingleWall(world, pos.south(), true)

        pos = originalPos
        pos = pos.east(6)
        pos = BlockPos(pos.x, getBaseHeight(world, pos.x, pos.z), pos.z)
        placeSingleWall(world, pos, true)
        placeSingleWall(world, pos.north(), true)
        placeSingleWall(world, pos.south(), true)

        return true
    }
}