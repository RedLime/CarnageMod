package com.redlimerl.carnage.registry

import com.redlimerl.carnage.CarnageMod
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup

class CarnageGroups {
    companion object {
        val CARNAGE_TAB: ItemGroup = FabricItemGroupBuilder.create(CarnageMod.identifier("group")).icon { CarnageItems.CARNAGE_INGOT.defaultStack }.build()
    }
}