package com.redlimerl.carnage.material.block

import com.redlimerl.carnage.entity.TheRuptureEntity
import com.redlimerl.carnage.registry.CarnageEntities
import com.redlimerl.carnage.registry.CarnageItems
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion

@Suppress("DEPRECATION")
class TheRuptureSpawnBlock(settings: Settings) : Block(settings) {

    override fun onUse(state: BlockState?, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hit: BlockHitResult?): ActionResult {
        if (player.mainHandStack.item == CarnageItems.TARTARUS && world.difficulty != Difficulty.PEACEFUL) {
            if (!player.isCreative) player.mainHandStack.decrement(1)
            world.setBlockState(pos, Blocks.AIR.defaultState)
            val di = world.dimension
            if (di.hasFixedTime() && !di.isUltrawarm && !di.isBedWorking && !di.isNatural && !di.isPiglinSafe && di.hasCeiling() && !di.hasEnderDragonFight()) {
                world.playSound(null, pos, SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.VOICE, 2.0f, 1.0f)
                world.playSound(null, pos, SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.VOICE, 2.0f, 0.6f)
                world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.VOICE, 2.0f, 1.0f)
                world.createExplosion(null, pos.x+0.5, pos.y+0.5, pos.z+0.5, 2.5f, Explosion.DestructionType.BREAK)
                world.spawnEntity(TheRuptureEntity(CarnageEntities.THE_RUPTURE, world, pos).apply { teleport(pos.x+0.5, pos.y+0.5, pos.z+0.5) })
                //CarnageEntities.THE_RUPTURE.spawn(world, null, null, null, pos, SpawnReason.EVENT, true, false)
            } else {
                world.createExplosion(null, pos.x+0.5, pos.y+0.5, pos.z+0.5, 6f, Explosion.DestructionType.DESTROY)
            }
            return ActionResult.CONSUME
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }
}