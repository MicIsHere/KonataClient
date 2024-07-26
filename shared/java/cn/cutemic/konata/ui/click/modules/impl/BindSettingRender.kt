package cn.cutemic.konata.ui.click.modules.impl

import org.lwjgl.input.Keyboard
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BindSetting
import cn.cutemic.konata.ui.click.MainPanel
import cn.cutemic.konata.ui.click.modules.SettingRender
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.render.Render2DUtils
import java.util.*

class BindSettingRender(module: Module, setting: BindSetting) : SettingRender<BindSetting>(setting) {
    private var colorAnimation = ColorAnimation()

    init {
        mod = module
    }

    override fun render(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        mouseX: Float,
        mouseY: Float,
        custom: Boolean
    ) {
        val fw = Konata.fontManager.s16.drawString(
            Konata.i18n[(mod.name + "." + setting.name).lowercase(
                Locale.getDefault()
            )], x + 10, y + 2, Konata.theme.textColorTitle.rgb
        ).toFloat()
        val keyName = Keyboard.getKeyName(setting.value)
        val s16b = Konata.fontManager.s16
        val width1 = 10 + s16b.getStringWidth(keyName)
        if (Render2DUtils.isHovered(x + 15 + fw, y, width1.toFloat(), 14f, mouseX.toInt(), mouseY.toInt())) {
            Render2DUtils.drawOptimizedRoundedRect(
                x + 14.5f + fw,
                y - 0.5f,
                (width1 + 1).toFloat(),
                13f,
                Konata.theme.modeBoxBorder
            )
        }
        Render2DUtils.drawOptimizedRoundedRect(x + 15 + fw, y, width1.toFloat(), 12f, colorAnimation.color)
        s16b.drawString(keyName, x + 18 + fw, y + 2, Konata.theme.textColorTitle.rgb)
        if (MainPanel.bindLock == setting.name) {
            colorAnimation.base(Konata.theme.modeBoxBorder)
        } else {
            colorAnimation.base(Konata.theme.modeBox)
        }
        this.height = 16f
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
        val fw = Konata.fontManager.s16.getStringWidth(
            Konata.i18n[(mod.name + "." + setting.name).lowercase(
                Locale.getDefault()
            )]
        ).toFloat()
        val keyName = Keyboard.getKeyName(setting.value)
        val s16b = Konata.fontManager.s16
        if (Render2DUtils.isHovered(
                x + 12 + fw,
                y,
                10f + s16b.getStringWidth(keyName),
                12f,
                mouseX.toInt(),
                mouseY.toInt()
            ) && btn == 0
        ) {
            if (MainPanel.bindLock.isEmpty()) {
                MainPanel.bindLock = setting.name
            }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (MainPanel.bindLock == setting.name) {
            setting.value = Keyboard.getEventKey()
            MainPanel.bindLock = ""
        }
    }
}
