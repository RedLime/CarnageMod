package com.redlimerl.carnage.biome

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.registry.CarnageBlocks
import com.redlimerl.carnage.registry.CarnageEntities
import com.redlimerl.carnage.structure.CarnageStructures
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeEffects
import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.biome.SpawnSettings
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig


object CarnageBiome {

    private val CADREGA_SURFACE_BUILDER = SurfaceBuilder.DEFAULT
            .withConfig(TernarySurfaceConfig(CarnageBlocks.CADREGA_PATCH_STONE.defaultState, CarnageBlocks.CADREGA_STONE.defaultState, CarnageBlocks.CADREGA_STONE.defaultState))

    val CADREGA_FALLS_KEY: RegistryKey<Biome> = RegistryKey.of(Registry.BIOME_KEY, CarnageMod.identifier("the_cadrega_falls"))

    fun registry() {
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, CarnageMod.identifier("the_cadrega_falls"), CADREGA_SURFACE_BUILDER)
        Registry.register(BuiltinRegistries.BIOME, CADREGA_FALLS_KEY.value, CADREGA_FALLS)

    }

    val CADREGA_FALLS = createCarnageDefault().build()

    private fun createCarnageDefault(): Biome.Builder {
        // We specify what entities spawn and what features generate in the biome.
        // Aside from some structures, trees, rocks, plants and
        //   custom entities, these are mostly the same for each biome.
        // Vanilla configured features for biomes are defined in DefaultBiomeFeatures.
        val spawnSettings = SpawnSettings.Builder()
        spawnSettings.spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(CarnageEntities.CARCANO, 10, 1, 1))
        spawnSettings.spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(CarnageEntities.COGNITION, 5, 1, 1))
        spawnSettings.spawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(CarnageEntities.KILLBOT, 1, 1, 1))
        spawnSettings.spawnCost(CarnageEntities.CARCANO, 0.25, 2.0)
        spawnSettings.spawnCost(CarnageEntities.COGNITION, 0.25, 2.0)
        spawnSettings.spawnCost(CarnageEntities.KILLBOT, 0.15, 2.0)

        val generationSettings = GenerationSettings.Builder()
        generationSettings.surfaceBuilder(CADREGA_SURFACE_BUILDER)
        generationSettings.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, CarnageStructures.BEDROCK_COLUMN_CONFIG)

        return Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.NONE)
                .depth(0.1f)
                .scale(0.8f)
                .temperature(0.2f)
                .downfall(0.5f)
                .effects(BiomeEffects.Builder()
                        .waterColor(0x0)
                        .waterFogColor(0x0)
                        .fogColor(0x0)
                        .skyColor(0x0)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
    }
}