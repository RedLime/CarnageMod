@file:Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")

package com.redlimerl.carnage.structure

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.biome.CarnageBiome
import com.redlimerl.carnage.structure.decorator.ChanceHeightmapDecorator
import com.redlimerl.carnage.structure.feature.BedrockColumnFeature
import com.redlimerl.carnage.structure.structure.CadregaDungeon
import com.redlimerl.carnage.structure.structure.CadregaDungeonGenerator
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder
import net.minecraft.structure.StructurePieceType
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.feature.*


object CarnageStructures {



    //DECORATOR
    val CHANCE_OCEAN_FLOOR_WG: Decorator<ChanceDecoratorConfig> = Registry.register(Registry.DECORATOR, CarnageMod.identifier("chance_heightmap_legacy"), ChanceHeightmapDecorator())

    //FEATURE
    val BEDROCK_COLUMN = BedrockColumnFeature()
    val BEDROCK_COLUMN_CONFIG: ConfiguredFeature<*, *> = BEDROCK_COLUMN.configure(FeatureConfig.DEFAULT)
            .decorate(CHANCE_OCEAN_FLOOR_WG.configure(ChanceDecoratorConfig(5)))

    //STRUCTURE
    val CADREGA_DUNGEON: StructureFeature<DefaultFeatureConfig> = CadregaDungeon()
    val CADREGA_DUNGEON_PIECE = StructurePieceType { structureManager, tag ->  CadregaDungeonGenerator.Piece(structureManager, tag) }
    val CADREGA_DUNGEON_CONFIG: ConfiguredStructureFeature<*, *> = CADREGA_DUNGEON.configure(DefaultFeatureConfig.DEFAULT)
    val CADREGA_DUNGEON_KEY: RegistryKey<ConfiguredStructureFeature<*, *>>
            = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, CarnageMod.identifier("cadrega_dungeon"))

    fun registerFeatures() {
        Registry.register(Registry.FEATURE, BedrockColumnFeature.ID, BEDROCK_COLUMN)
    }

    fun putFeatures() {
        /*
        RegistrationHelper.addToBiome(BedrockColumnFeature.ID,
            BiomeSelectors.includeByKey(RegistryKey.of(Registry.BIOME_KEY,
                    CarnageMod.identifier("cadrega_planet")), RegistryKey.of(Registry.BIOME_KEY, CarnageMod.identifier("cadrega_canyon"))),
                Consumer { context -> RegistrationHelper.addFeature(context, CarnageConfiguredFeatures.BEDROCK_COLUMN)})

         */
    }

    fun registerStructures() {
        Registry.register(Registry.STRUCTURE_PIECE, CarnageMod.identifier("cadrega_dungeon_piece"), CADREGA_DUNGEON_PIECE)
        FabricStructureBuilder.create(CarnageMod.identifier("cadrega_dungeon"), CADREGA_DUNGEON)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(48, 24, 12345)
                .adjustsSurface()
                .register()

    }

    fun registerConfiguredFeatures() {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CarnageMod.identifier("configured_bedrock_column"), BEDROCK_COLUMN_CONFIG)

        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, CADREGA_DUNGEON_KEY.value, CADREGA_DUNGEON_CONFIG)
    }

    fun putStructures() {
        BiomeModifications.addStructure(BiomeSelectors.includeByKey(CarnageBiome.CADREGA_FALLS_KEY), CADREGA_DUNGEON_KEY)
    }
}
