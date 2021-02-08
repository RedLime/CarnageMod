@file:Suppress("DEPRECATION")

package com.redlimerl.carnage

import com.redlimerl.carnage.client.CarnageClientJavaRegistry
import com.redlimerl.carnage.biome.CarnageBiome
import com.redlimerl.carnage.gui.CarnageCraftingTableScreenHandler
import com.redlimerl.carnage.material.block.CarnageCraftingTable
import com.redlimerl.carnage.registry.*
import com.redlimerl.carnage.structure.CarnageStructures
import com.redlimerl.carnage.util.OreGeneration
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep


class CarnageMod : ModInitializer, ClientModInitializer {

    companion object {
        val MODID = "carnage"

        fun identifier(name: String): Identifier {
            return Identifier(MODID, name)
        }

        val CCT_SCREEN_HANDLER_TYPE: ScreenHandlerType<CarnageCraftingTableScreenHandler> =
                ScreenHandlerRegistry.registerSimple(CarnageCraftingTable.ID) { syncId: Int, playerInventory: PlayerInventory ->
                    CarnageCraftingTableScreenHandler(syncId, playerInventory)
        }
    }

    override fun onInitialize() {

        CarnageItems.registry()
        CarnageBlocks.registry()

        CarnageLoots.registry()

        CarnageBiome.registry()

        CarnageBlockEntities.registry()
        CarnageEntities.serverRegistry()

        CarnageEnchantments.registry()

        CarnageStructures.registerFeatures()
        CarnageStructures.registerStructures()

        CarnageStructures.registerConfiguredFeatures()

        CarnageStructures.putFeatures()
        CarnageStructures.putStructures()

        val carnageOreOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, identifier("ore_carnage_overworld"))
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, carnageOreOverworld.value, OreGeneration.ORE_CARNAGE_OVERWORLD)

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld()
            /*.and { it.biomeKey == CarnageBiome.OBSILAND_KEY }*/, GenerationStep.Feature.UNDERGROUND_ORES, carnageOreOverworld)

        CustomPortalApiRegistry.addPortal(CarnageBlocks.CADREGA_PORTAL, PortalIgnitionSource.ItemUseSource(CarnageItems.KEY_OF_CADREGA), identifier("the_cadrega"), 45, 0, 0)
    }

    override fun onInitializeClient() {
        CarnageEntities.registry()
        CarnageClientJavaRegistry.registry()
    }
}