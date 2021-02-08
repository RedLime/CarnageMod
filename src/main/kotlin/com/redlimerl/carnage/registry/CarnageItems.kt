@file:Suppress("MemberVisibilityCanBePrivate")

package com.redlimerl.carnage.registry

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.material.*
import com.redlimerl.carnage.material.item.CadregaTotem
import com.redlimerl.carnage.material.item.LifeFruitItem
import com.redlimerl.carnage.material.item.ZirokabiBundleItem
import com.redlimerl.carnage.registry.CarnageGroups.Companion.CARNAGE_TAB
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.*
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry


class CarnageItems {
    private val group = Item.Settings().group(CARNAGE_TAB)

    companion object {
        val CARNAGE_INGOT = Item(CarnageItems().group.fireproof())
        val CARNAGE_PIECE = Item(CarnageItems().group)
        val CARNAGE_SHARD = Item(CarnageItems().group)
        val CADREGA_TOTEM = EnchantedGoldenAppleItem(CarnageItems().group.maxCount(1))

        val CARNAGE_PICKAXE = CarnagePickaxeItem(CarnageMaterials.INSTANCE, 7, -2.6f, CarnageItems().group.fireproof())
        val CARNAGE_AXE = CarnageAxeItem(CarnageMaterials.INSTANCE, 11.5f, -2.8f, CarnageItems().group.fireproof())
        val CARNAGE_SWORD = CarnageSwordItem(CarnageMaterials.INSTANCE, 10, -2f, CarnageItems().group.fireproof())
        val CARNAGE_SHOVEL = CarnageShovelItem(CarnageMaterials.INSTANCE, 7.5f, -2.5f, CarnageItems().group.fireproof())
        val CARNAGE_HOE = CarnageShovelItem(CarnageMaterials.INSTANCE, 4f, 0f, CarnageItems().group.fireproof())

        val CARNAGE_HELMET = ArmorItem(CarnageArmorMaterials.INSTANCE, EquipmentSlot.HEAD, CarnageItems().group.fireproof())
        val CARNAGE_CHESTPLATE = ArmorItem(CarnageArmorMaterials.INSTANCE, EquipmentSlot.CHEST, CarnageItems().group.fireproof())
        val CARNAGE_LEGGINGS = ArmorItem(CarnageArmorMaterials.INSTANCE, EquipmentSlot.LEGS, CarnageItems().group.fireproof())
        val CARNAGE_BOOTS = ArmorItem(CarnageArmorMaterials.INSTANCE, EquipmentSlot.FEET, CarnageItems().group.fireproof())

        val END_SHARD = Item(CarnageItems().group)

        val CARCANO_SPAWN_EGG = SpawnEggItem(CarnageEntities.CARCANO, 0x707070, 0x214700, CarnageItems().group)
        val KILLBOT_SPAWN_EGG = SpawnEggItem(CarnageEntities.KILLBOT, 0x627560, 0x4a9e00, CarnageItems().group)
        val COGNITION_SPAWN_EGG = SpawnEggItem(CarnageEntities.COGNITION, 0x707070, 0x3b3b3b, CarnageItems().group)
        val ZIROKABI_SPAWN_EGG = SpawnEggItem(CarnageEntities.ZIROKABI, 0x707070, 0x4a9e00, CarnageItems().group)

        val SHARD_OF_TARTARUS = EnchantedGoldenAppleItem(CarnageItems().group)
        val TARTARUS = EnchantedGoldenAppleItem(CarnageItems().group.fireproof())

        val KEY_OF_CADREGA = EnchantedGoldenAppleItem(CarnageItems().group)

        val CARCANO_FRAGMENT = Item(CarnageItems().group)
        val COGNITION_FRAGMENT = Item(CarnageItems().group)
        val KILLBOT_FRAGMENT = Item(CarnageItems().group)
        val ZIROKABI_MEMBRANE = Item(CarnageItems().group)

        val ZIROKABI_BUNDLE = ZirokabiBundleItem(CarnageItems().group.maxCount(1).fireproof())


        val REDLIME_INGOT = Item(CarnageItems().group.fireproof().rarity(Rarity.EPIC))
        val LIFE_FRUIT = LifeFruitItem(CarnageItems().group.fireproof().rarity(Rarity.EPIC))


        fun registry() {
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_ingot"), CARNAGE_INGOT)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_piece"), CARNAGE_PIECE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_pickaxe"), CARNAGE_PICKAXE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_axe"), CARNAGE_AXE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_sword"), CARNAGE_SWORD)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_shovel"), CARNAGE_SHOVEL)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_hoe"), CARNAGE_HOE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_helmet"), CARNAGE_HELMET)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_chestplate"), CARNAGE_CHESTPLATE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_leggings"), CARNAGE_LEGGINGS)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_boots"), CARNAGE_BOOTS)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carnage_shard"), CARNAGE_SHARD)
            Registry.register(Registry.ITEM, CarnageMod.identifier("end_shard"), END_SHARD)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carcano_spawn_egg"), CARCANO_SPAWN_EGG)
            Registry.register(Registry.ITEM, CarnageMod.identifier("killbot_spawn_egg"), KILLBOT_SPAWN_EGG)
            Registry.register(Registry.ITEM, CarnageMod.identifier("cognition_spawn_egg"), COGNITION_SPAWN_EGG)
            Registry.register(Registry.ITEM, CarnageMod.identifier("zirokabi_spawn_egg"), ZIROKABI_SPAWN_EGG)
            Registry.register(Registry.ITEM, CarnageMod.identifier("totem_of_cadrega"), CADREGA_TOTEM)
            Registry.register(Registry.ITEM, CarnageMod.identifier("shard_of_tartarus"), SHARD_OF_TARTARUS)
            Registry.register(Registry.ITEM, CarnageMod.identifier("tartarus"), TARTARUS)
            Registry.register(Registry.ITEM, CarnageMod.identifier("key_of_cadrega"), KEY_OF_CADREGA)
            Registry.register(Registry.ITEM, CarnageMod.identifier("carcano_fragment"), CARCANO_FRAGMENT)
            Registry.register(Registry.ITEM, CarnageMod.identifier("cognition_fragment"), COGNITION_FRAGMENT)
            Registry.register(Registry.ITEM, CarnageMod.identifier("killbot_fragment"), KILLBOT_FRAGMENT)
            Registry.register(Registry.ITEM, CarnageMod.identifier("zirokabi_membrane"), ZIROKABI_MEMBRANE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("zirokabi_bundle"), ZIROKABI_BUNDLE)
            Registry.register(Registry.ITEM, CarnageMod.identifier("redlime"), REDLIME_INGOT)
            Registry.register(Registry.ITEM, CarnageMod.identifier("life_fruit"), LIFE_FRUIT)
        }
    }
}