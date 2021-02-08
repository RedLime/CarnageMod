package com.redlimerl.carnage.registry

import com.redlimerl.carnage.CarnageMod.Companion.identifier
import com.redlimerl.carnage.client.*
import com.redlimerl.carnage.entity.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object CarnageEntities {


    val CARCANO: EntityType<CarcanoEntity> =
            Registry.register(Registry.ENTITY_TYPE, identifier("carcano"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::CarcanoEntity).dimensions(EntityDimensions.fixed(0.6f, 1.95f)).build())

    val KILLBOT: EntityType<KillbotEntity> =
        Registry.register(Registry.ENTITY_TYPE, identifier("killbot"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::KillbotEntity).dimensions(EntityDimensions.fixed(0.6f, 1.7f)).build())

    val COGNITION: EntityType<CognitionEntity> =
            Registry.register(Registry.ENTITY_TYPE, identifier("cognition"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::CognitionEntity).dimensions(EntityDimensions.fixed(0.6f, 1.99f)).build())


    val ZIROKABI: EntityType<ZirokabiEntity> =
            Registry.register(Registry.ENTITY_TYPE, identifier("zirokabi"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::ZirokabiEntity).dimensions(EntityDimensions.fixed(2.7f, 1.2f)).build())

    val THE_RUPTURE: EntityType<TheRuptureEntity> =
            Registry.register(Registry.ENTITY_TYPE, identifier("the_rupture"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::TheRuptureEntity).dimensions(EntityDimensions.fixed(1f, 2f)).build())


    fun registry() {
        EntityRendererRegistry.INSTANCE.register(CARCANO, ::CarcanoRenderer)
        EntityRendererRegistry.INSTANCE.register(KILLBOT, ::KillbotRenderer)
        EntityRendererRegistry.INSTANCE.register(COGNITION, ::CognitionRenderer)
        EntityRendererRegistry.INSTANCE.register(ZIROKABI, ::ZirokabiRenderer)
        EntityRendererRegistry.INSTANCE.register(THE_RUPTURE, ::TheRuptureRenderer)
    }


    fun serverRegistry() {
        FabricDefaultAttributeRegistry.register(CARCANO, CarcanoEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(KILLBOT, KillbotEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(COGNITION, CognitionEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(ZIROKABI, ZirokabiEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(THE_RUPTURE, TheRuptureEntity.createAttributes())
    }
}