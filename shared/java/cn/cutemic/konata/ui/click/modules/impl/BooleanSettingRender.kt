package cn.cutemic.konata.ui.click.modules.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.ui.click.modules.SettingRender
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.math.animation.Type
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.util.*

class BooleanSettingRender(mod: Module, setting: BooleanSetting) : SettingRender<BooleanSetting>(setting) {
    // animation
    private var box = ColorAnimation(Color(255, 255, 255, 0))

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
        box.update()
        if (setting.value) {
            box.start(box.color, Konata.theme.primary, 0.2f, Type.EASE_IN_OUT_QUAD)
        } else {
            box.start(box.color, Konata.theme.checkboxBox, 0.2f, Type.EASE_IN_OUT_QUAD)
        }
        Render2DUtils.drawOptimizedRoundedRect(x + 14, y + 3, 6f, 6f, 3, box.color.rgb)
        Konata.fontManager.s16.drawString(
            Konata.i18n[(mod.name + "." + setting.name).lowercase(
                Locale.getDefault()
            )], x + 26, y + 1, Konata.theme.textColorDescription.rgb
        )
        this.height = 12f
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
        if (Render2DUtils.isHovered(x, y, width, height, mouseX.toInt(), mouseY.toInt())) {
            setting.toggle()
        }
    }
}
