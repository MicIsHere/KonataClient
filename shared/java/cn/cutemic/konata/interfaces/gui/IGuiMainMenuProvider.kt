package cn.cutemic.konata.interfaces.gui

import net.minecraft.client.gui.GuiScreen
import cn.cutemic.konata.interfaces.IProvider

interface IGuiMainMenuProvider : IProvider {
    fun initGui()
    fun renderSkybox(mouseX: Int, mouseY: Int, partialTicks: Float, width: Int, height: Int, zLevel: Float)
    fun showSinglePlayer(screen: GuiScreen)
}