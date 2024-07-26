package cn.cutemic.konata.interfaces.gui

import net.minecraft.entity.Entity

interface IGuiIngameProvider {
    fun drawHealth(entityIn: Entity)
}