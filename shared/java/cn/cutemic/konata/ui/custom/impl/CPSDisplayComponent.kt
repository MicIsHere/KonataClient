package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.CPSDisplay
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.wrapper.TextFormattingProvider

class CPSDisplayComponent : Component(CPSDisplay::class.java) {
    init {
        x = 0.05f
        y = 0.05f
    }

    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s16 = Konata.fontManager.s16
        val text =
            "CPS: " + CPSDisplay.lcps + TextFormattingProvider.getGray() + " | " + TextFormattingProvider.getReset() + CPSDisplay.rcps
        width = getStringWidth(s16, text) + 4
        height = 14f
        drawRect(x - 2, y, width, height, mod.backgroundColor.color)
        drawString(s16, text, x, y + 2, CPSDisplay.textColor.rGB)
    }
}
