package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class FPSDisplay : InterfaceModule("FPSDisplay", Category.Interface) {
    init {
        addSettings(textColor)
        addSettings(rounded, backgroundColor, fontShadow, betterFont, bg, rounded , roundRadius)
    }

    companion object {
        @JvmField
        var textColor = ColorSetting("TextColor", Color(255, 255, 255))
    }
}
