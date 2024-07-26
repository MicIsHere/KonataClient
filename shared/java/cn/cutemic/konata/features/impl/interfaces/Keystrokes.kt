package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class Keystrokes : InterfaceModule("Keystrokes", Category.Interface) {
    init {
        addSettings(rounded, backgroundColor, fontShadow, betterFont, pressedColor, bg, rounded , roundRadius)
    }

    companion object {
        @JvmField
        var pressedColor = ColorSetting("PressedColor", Color(255, 255, 255, 120))
    }
}
