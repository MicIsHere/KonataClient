package cn.cutemic.konata.ui.click.modules.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.TextSetting
import cn.cutemic.konata.ui.common.TextField
import cn.cutemic.konata.ui.click.modules.SettingRender
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.util.*
import kotlin.math.min

class TextSettingRender(mod: Module, setting: TextSetting) : SettingRender<TextSetting>(setting) {
    private var inputBox: TextField

    init {
        this.mod = mod
        inputBox = TextField(Konata.fontManager.s16, false, "输入名称", -1, Color(50, 50, 50).rgb, 1500)
        inputBox.text = setting.value
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
        inputBox.backGroundColor = Konata.theme.textboxEnabled.rgb
        inputBox.fontColor = Konata.theme.textColorTitle.rgb
        val s16 = Konata.fontManager.s16
        val text = Konata.i18n[(mod.name + "." + setting.name).lowercase(Locale.getDefault())]
        s16.drawString(text, x + 18, y + 6, Konata.theme.textColorDescription.rgb)
        inputBox.drawTextBox(
            x + s16.getStringWidth(text) + 20,
            y + 2,
            min(200f, Konata.fontManager.s18.getStringWidth(inputBox.text) + 20f),
            16f
        )
        this.height = 24f
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
        if (Render2DUtils.isHovered(x, y, width, height, mouseX.toInt(), mouseY.toInt())) {
            inputBox.mouseClicked(mouseX.toInt(), mouseY.toInt(), btn)
        } else {
            inputBox.setFocused(false)
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        inputBox.textboxKeyTyped(typedChar, keyCode)
        setting.value = inputBox.text
    }
}
