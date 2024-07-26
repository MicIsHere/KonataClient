package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.ReachDisplay
import cn.cutemic.konata.ui.custom.Component

class ReachDisplayComponent : Component(ReachDisplay::class.java) {
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s = "${ReachDisplay.reach} b"
        val s18 = Konata.fontManager.s18
        width = getStringWidth(s18, s) + 4
        height = 14f
        drawRect(x - 2, y, width, height, mod.backgroundColor.color)
        drawString(s18, s, x, y + 2, ReachDisplay.textColor.rGB)
    }
}
