package com.redlimerl.carnage.client

import com.redlimerl.carnage.CarnageMod.Companion.CCT_SCREEN_HANDLER_TYPE
import com.redlimerl.carnage.gui.CarnageCraftingTableScreen
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.screen.ScreenHandler

object CarnageClientJavaRegistry {

    fun registry() {
        ScreenRegistry.register<ScreenHandler, CarnageCraftingTableScreen>(CCT_SCREEN_HANDLER_TYPE, ::CarnageCraftingTableScreen)
    }
}