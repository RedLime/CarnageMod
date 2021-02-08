package com.redlimerl.carnage.material

import net.minecraft.item.*

class CarnagePickaxeItem(material: ToolMaterial, attackDamage: Int, attackSpeed: Float, settings: Settings) : PickaxeItem(material, attackDamage, attackSpeed, settings)
class CarnageAxeItem(material: ToolMaterial, attackDamage: Float, attackSpeed: Float, settings: Settings) : AxeItem(material, attackDamage, attackSpeed, settings)
class CarnageHoeItem(material: ToolMaterial, attackDamage: Int, attackSpeed: Float, settings: Settings) : HoeItem(material, attackDamage, attackSpeed, settings)
class CarnageSwordItem(toolMaterial: ToolMaterial, attackDamage: Int, attackSpeed: Float, settings: Settings) : SwordItem(toolMaterial, attackDamage, attackSpeed, settings)
class CarnageShovelItem(material: ToolMaterial, attackDamage: Float, attackSpeed: Float, settings: Settings) : ShovelItem(material, attackDamage, attackSpeed, settings)