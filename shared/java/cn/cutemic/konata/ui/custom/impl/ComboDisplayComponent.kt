package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.ComboDisplay
import cn.cutemic.konata.ui.custom.Component

class ComboDisplayComponent : Component(ComboDisplay::class.java) {
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s16 = Konata.fontManager.s16
        var text = "Combo: " + ComboDisplay.combo
        if (ComboDisplay.combo == 0) {
            text = "No Combo"
        }
        width = getStringWidth(s16, text) + 4
        height = 16f
        drawRect(x - 2, y, width, height, mod.backgroundColor.color)
        drawString(s16, text, x, y + 4, ComboDisplay.textColor.rGB)
    }
}
