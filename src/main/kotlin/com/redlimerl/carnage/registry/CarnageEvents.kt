package com.redlimerl.carnage.registry

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.minecraft.loot.condition.*
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProviderTypes
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.util.Identifier


class CarnageEvents {
    companion object {
        fun registry() {
            LootTableLoadingCallback.EVENT.register(LootTableLoadingCallback { resourceManager, lootManager, identifier, fabricLootSupplierBuilder, lootTableSetter ->

                //Enderman's End Piece Loot Table
                if (identifier == Identifier("minecraft", "entities/enderman")) {
                    val poolBuilder: FabricLootPoolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1f))
                        .withEntry(ItemEntry.builder(CarnageItems.END_PIECE).build())
                        .withCondition(AlternativeLootCondition.builder(
                            KilledByPlayerLootCondition.builder(),
                            RandomChanceWithLootingLootCondition.builder(
                                0.03f, 0.125f
                            )
                        ).build())


                    fabricLootSupplierBuilder.withPool(poolBuilder.build())
                }

                //End city treasure Chest Loot Table
                if (identifier == Identifier("minecraft", "chests/end_city_treasure")) {
                    val poolBuilder: FabricLootPoolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1f))
                        .withEntry(ItemEntry.builder(CarnageItems.END_SHARD).build())
                        .withCondition(RandomChanceLootCondition.builder(0.2f).build())
                        .withFunction(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 3f)).build())

                    fabricLootSupplierBuilder.withPool(poolBuilder.build())
                }
            })
        }
    }
}