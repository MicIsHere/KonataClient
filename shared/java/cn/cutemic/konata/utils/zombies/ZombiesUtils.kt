package cn.cutemic.konata.utils.zombies

import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemHoe
import net.minecraft.item.ItemSpade

object ZombiesUtils {

    fun isWeapon(item: Item): Boolean {
        return when (item){
            is ItemHoe -> {
                true
            }

            Items.diamond_shovel -> {
                false
            }

            is ItemSpade -> {
                true
            }

            Items.diamond_pickaxe -> {
                true
            }

            Items.golden_pickaxe -> {
                true
            }

            Items.shears -> {
                true
            }

            Items.flint_and_steel -> {
                true
            }

            else -> {
                false
            }
        }
    }

    fun isEnemy(item: Item): Boolean{
        return item == Items.dye
    }

}