package com.redlimerl.carnage.material.block

import net.minecraft.block.*
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape


class CarnageStairsBlock(baseBlockState: BlockState, settings: Settings) : StairsBlock(baseBlockState, settings)

open class CarnageFacingBlock(settings: Settings) : HorizontalFacingBlock(settings) {

    companion object {
        var FACING: DirectionProperty = HorizontalFacingBlock.FACING
    }

    init {
        defaultState = (stateManager.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(FACING, ctx.playerFacing.opposite)
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState? {
        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState? {
        return state.rotate(mirror.getRotation(state.get(FACING) as Direction))
    }
}