package cn.cutemic.konata.ui.custom.impl

import net.minecraft.client.Minecraft
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.FPSDisplay
import cn.cutemic.konata.ui.custom.Component

class FPSDisplayComponent : Component(FPSDisplay::class.java) {
    init {
        x = 0.05f
        y = 0.05f
    }

    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s = Minecraft.getDebugFPS().toString() + "fps"
        val s18 = Konata.fontManager.s18
        width = getStringWidth(s18, s) + 4
        height = 14f
        drawRect(x - 2, y, width, height, mod.backgroundColor.color)
        drawString(s18, s, x, y + 2, FPSDisplay.textColor.rGB)
    }
}
