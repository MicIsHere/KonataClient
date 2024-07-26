package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.PingDisplay
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.interfaces.ProviderManager

class PingDisplayComponent : Component(PingDisplay::class.java) {
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s16 = Konata.fontManager.s16
        // get ping of connection
        if (ProviderManager.mcProvider.getPlayer() == null) return
        val ping = "${ProviderManager.mcProvider.getRespondTime()}ms"
        val text = "Ping: $ping"
        width = getStringWidth(s16, text) + 4
        height = 14f
        drawRect(x - 2, y, width, height, mod.backgroundColor.color)
        drawString(s16, text, x, y + 2, PingDisplay.textColor.rGB)
    }
}