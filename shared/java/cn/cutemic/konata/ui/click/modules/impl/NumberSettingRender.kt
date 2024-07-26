package cn.cutemic.konata.ui.click.modules.impl

import org.lwjgl.input.Mouse
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.ui.click.MainPanel
import cn.cutemic.konata.ui.click.modules.SettingRender
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.render.Render2DUtils
import java.util.*

class NumberSettingRender(mod: Module, setting: NumberSetting) : SettingRender<NumberSetting>(setting) {
    // animation
    private var aWidth = 0f
    private var dragging = false

    init {
        this.mod = mod
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
            )], x + 10, y + 2, Konata.theme.textColorDescription.rgb
        ).toFloat()
        Render2DUtils.drawOptimizedRoundedRect(x + 16 + fw, y + 3, 160f, 6f, Konata.theme.frontBackground.rgb)
        val percent =
            (setting.value.toFloat() - setting.min.toFloat()) / (setting.max.toFloat() - setting.min.toFloat())
        aWidth = base(aWidth.toDouble(), (160 * percent).toDouble(), 0.2).toFloat()
        Render2DUtils.drawOptimizedRoundedRect(x + 16 + fw, y + 3, aWidth, 6f, Konata.theme.primary.rgb)
        Konata.fontManager.s16.drawString(
            setting.value.toString(),
            x + fw + 20 + 160,
            y + 2,
            Konata.theme.textNumber.rgb
        )
        if (!Mouse.isButtonDown(0)) MainPanel.dragLock = "null"
        if (MainPanel.dragLock == mod.name + setting.name + 4) {
            val v = mouseX - x - 16 - Konata.fontManager.s16.getStringWidth(
                Konata.i18n[(mod.name + "." + setting.name).lowercase(
                    Locale.getDefault()
                )]
            )
            val mPercent = v / 160
            val newValue = (setting.max.toFloat() - setting.min.toFloat()) * mPercent + setting.min.toFloat()
            setting.value = newValue
        }
        this.height = 12f
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
        val fw = Konata.fontManager.s16.drawString(
            Konata.i18n[(mod.name + "." + setting.name).lowercase(
                Locale.getDefault()
            )], x + 10, y + 2, Konata.theme.textColorDescription.rgb
        ).toFloat()
        if (Render2DUtils.isHovered(x + 16 + fw, y, 160f, height, mouseX.toInt(), mouseY.toInt()) && Mouse.isButtonDown(0)) {
            if (btn == 0 && MainPanel.dragLock == "null") {
                MainPanel.dragLock = mod.name + setting.name + 4
                dragging = true
            }
        }
    }
}
