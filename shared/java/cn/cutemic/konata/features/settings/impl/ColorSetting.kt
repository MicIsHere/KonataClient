package cn.cutemic.konata.features.settings.impl

import cn.cutemic.konata.features.settings.Setting
import cn.cutemic.konata.features.settings.impl.utils.CustomColor
import java.awt.Color

class ColorSetting : Setting<CustomColor> {

    constructor(name: String, value: CustomColor, visible: () -> Boolean) : super(name, value, visible) {
        this.visible = visible
    }
    constructor(name: String, value: Color, visible: () -> Boolean) : super(name, CustomColor(value), visible) {
        this.visible = visible
    }
    constructor(name: String, value: CustomColor) : super(name, value)
    constructor(name: String, value: Color) : super(name, CustomColor(value))

    val rGB: Int
        get() = value.rGB
    val color: Color
        get() = value.color
}
