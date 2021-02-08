package com.redlimerl.carnage.client

import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.entity.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Arm
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f

class CarcanoRenderer(context: EntityRendererFactory.Context)
    : MobEntityRenderer<CarcanoEntity, PlayerEntityModel<CarcanoEntity>>(context, PlayerEntityModel(context.getPart(EntityModelLayers.PLAYER), false), 0.6f) {

    override fun getTexture(entity: CarcanoEntity?): Identifier {
        return CarnageMod.identifier("textures/mobs/carnage_zombie.png")
    }
}

class KillbotRenderer(context: EntityRendererFactory.Context)
    : MobEntityRenderer<KillbotEntity, CreeperEntityModel<KillbotEntity>>(context, CreeperEntityModel(context.getPart(EntityModelLayers.CREEPER)), 0.6f) {

    override fun getTexture(entity: KillbotEntity?): Identifier {
        return CarnageMod.identifier("textures/mobs/carnage_creeper.png")
    }
}

class CognitionRenderer(context: EntityRendererFactory.Context)
    : MobEntityRenderer<CognitionEntity, SkeletonEntityModel<CognitionEntity>>(context, SkeletonEntityModel(context.getPart(EntityModelLayers.SKELETON)), 0.6f) {

    override fun getTexture(entity: CognitionEntity?): Identifier {
        return CarnageMod.identifier("textures/mobs/carnage_skeleton.png")
    }
}

class ZirokabiRenderer(context: EntityRendererFactory.Context)
    : MobEntityRenderer<ZirokabiEntity, PhantomEntityModel<ZirokabiEntity>>(context, PhantomEntityModel(context.getPart(EntityModelLayers.PHANTOM)), 2f) {


    override fun getTexture(entity: ZirokabiEntity?): Identifier {
        return CarnageMod.identifier("textures/mobs/zirokabi.png")
    }

    override fun scale(entity: ZirokabiEntity, matrices: MatrixStack, amount: Float) {
        val g = 1.95f
        matrices.scale(g, g, g)
        matrices.translate(0.0, 1.3125, 0.1875)
        super.scale(entity, matrices, amount)
    }

    override fun setupTransforms(entity: ZirokabiEntity, matrices: MatrixStack, animationProgress: Float, bodyYaw: Float, tickDelta: Float) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta)
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
    }

}

@Environment(EnvType.CLIENT)
class TheRuptureRenderer(context: EntityRendererFactory.Context) : BipedEntityRenderer<TheRuptureEntity, BipedEntityModel<TheRuptureEntity>>(context, TheRuptureEntityModel(context.getPart(EntityModelLayers.VEX)), 0.9f) {

    companion object {
        private val TEXTURE = CarnageMod.identifier("textures/mobs/the_rupture.png")
        private val CHARGING_TEXTURE = CarnageMod.identifier("textures/mobs/the_rupture_anger.png")
    }

    override fun getBlockLight(vexEntity: TheRuptureEntity, blockPos: BlockPos): Int {
        return 15
    }

    override fun getTexture(vexEntity: TheRuptureEntity): Identifier {
        return if (vexEntity.isCharging) CHARGING_TEXTURE else TEXTURE
    }

    override fun scale(vexEntity: TheRuptureEntity, matrixStack: MatrixStack, f: Float) {
        val i = 2
        val g = 1.0f + 0.15f * i.toFloat()
        matrixStack.scale(g, g, g)
        matrixStack.translate(0.0, 0.3, 0.1875)
    }

    override fun setupTransforms(entity: TheRuptureEntity, matrixStack: MatrixStack, f: Float, g: Float, h: Float) {
        super.setupTransforms(entity, matrixStack, f, g, h)
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.pitch))
    }

    class TheRuptureEntityModel(modelPart: ModelPart) : BipedEntityModel<TheRuptureEntity>(modelPart) {
        private val leftWing: ModelPart
        private val rightWing: ModelPart
        override fun getBodyParts(): Iterable<ModelPart> {
            return Iterables.concat(super.getBodyParts(), ImmutableList.of(rightWing, leftWing))
        }

        override fun setAngles(vexEntity: TheRuptureEntity, f: Float, g: Float, h: Float, i: Float, j: Float) {
            super.setAngles(vexEntity, f, g, h, i, j)
            if (vexEntity.isCharging) {
                when {
                    vexEntity.mainHandStack.isEmpty -> {
                        rightArm.pitch = 4.712389f
                        leftArm.pitch = 4.712389f
                    }
                    vexEntity.mainArm == Arm.RIGHT -> {
                        rightArm.pitch = 3.7699115f
                    }
                    else -> {
                        leftArm.pitch = 3.7699115f
                    }
                }
            }
            val var10000 = rightLeg
            var10000.pitch += 0.62831855f
            rightWing.pivotZ = 2.0f
            leftWing.pivotZ = 2.0f
            rightWing.pivotY = 1.0f
            leftWing.pivotY = 1.0f
            rightWing.yaw = 0.47123894f + MathHelper.cos(h * 0.8f) * 3.1415927f * 0.05f
            leftWing.yaw = -rightWing.yaw
            leftWing.roll = -0.47123894f
            leftWing.pitch = 0.47123894f
            rightWing.pitch = 0.47123894f
            rightWing.roll = 0.47123894f
        }

        init {
            leftLeg.visible = false
            hat.visible = false
            rightWing = modelPart.getChild("right_wing")
            leftWing = modelPart.getChild("left_wing")
        }
    }

}
