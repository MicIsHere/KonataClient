package cn.cutemic.konata.ui.click.modules

import cn.cutemic.konata.features.settings.Setting

open class SettingRender<T: Setting<*>>(var setting: T) : ValueRender() {
    override fun render(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        mouseX: Float,
        mouseY: Float,
        custom: Boolean
    ) {
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
    }
}