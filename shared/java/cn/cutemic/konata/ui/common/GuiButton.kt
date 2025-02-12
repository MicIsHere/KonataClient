package cn.cutemic.konata.ui.common

import cn.cutemic.konata.Konata
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color

open class GuiButton(var text: String, var runnable: Runnable) {
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    private var btnColor = ColorAnimation(Color(113, 127, 254))
    open fun render(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        if (Render2DUtils.isHovered(x, y, width, height, mouseX.toInt(), mouseY.toInt())) {
            btnColor.base(Color(135, 147, 255))
        } else {
            btnColor.base(Color(113, 127, 254))
        }
        Render2DUtils.drawOptimizedRoundedRect(x, y, width, height, btnColor.color)
        Konata.fontManager.s18.drawCenteredString(
            Konata.i18n[text],
            x + width / 2,
            y + height / 2 - 4,
            Konata.theme.buttonText.rgb
        )
    }

    open fun mouseClick(mouseX: Float, mouseY: Float, btn: Int) {
        if (Render2DUtils.isHovered(x, y, width, height, mouseX.toInt(), mouseY.toInt()) && btn == 0) {
            runnable.run()
        }
    }
}
