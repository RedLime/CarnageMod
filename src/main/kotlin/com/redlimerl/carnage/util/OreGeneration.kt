package com.redlimerl.carnage.util

import com.redlimerl.carnage.registry.CarnageBlocks
import net.minecraft.world.gen.decorator.Decorator
import net.minecraft.world.gen.decorator.RangeDecoratorConfig
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.OreFeatureConfig


class OreGeneration {
    companion object {
        val ORE_CARNAGE_OVERWORLD: ConfiguredFeature<*, *> = Feature.ORE
                .configure(OreFeatureConfig(
                        OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                        CarnageBlocks.CARNAGE_ORE.defaultState,
                        4)) // vein size
                .decorate(Decorator.RANGE.configure(RangeDecoratorConfig(
                        0,  // bottom offset
                        0,  // min y level
                        12))) // max y level
                .spreadHorizontally()
                .repeat(2) // number of veins per chunk
    }

}