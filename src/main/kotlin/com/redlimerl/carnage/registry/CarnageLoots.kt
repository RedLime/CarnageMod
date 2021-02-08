package com.redlimerl.carnage.registry

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback.LootTableSetter
import net.minecraft.loot.LootManager
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.LootFunction
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier


class CarnageLoots {
    companion object {
        private val END_CITY_LOOT_TABLE_ID = Identifier("minecraft", "chests/end_city_treasure")

        fun registry() {/*
            LootTableLoadingCallback.EVENT.register(LootTableLoadingCallback { _: ResourceManager?, _: LootManager?, id: Identifier?, supplier: FabricLootSupplierBuilder, _: LootTableSetter? ->
                if (END_CITY_LOOT_TABLE_ID == id) {
                    val poolBuilder: FabricLootPoolBuilder = FabricLootPoolBuilder.builder()
                            .withEntry(ItemEntry.builder(CarnageItems.END_SHARD.asItem()).weight(3).build())
                    supplier.withPool(poolBuilder.build())
                }
            })*/
        }
    }
}
