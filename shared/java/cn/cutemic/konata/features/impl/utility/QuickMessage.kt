package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.ui.screens.quickMessage.GuiQuickMessages

class QuickMessage:Module("QuickMessage",Category.Utility) {
    override fun onEnable() {
        super.onEnable()
        mc.displayGuiScreen(GuiQuickMessages())
        set(false)
    }

}