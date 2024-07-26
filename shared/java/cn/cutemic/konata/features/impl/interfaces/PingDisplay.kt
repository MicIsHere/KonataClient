package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class PingDisplay : InterfaceModule("PingDisplay", Category.Interface) {
    init {
        addSettings(textColor)
        addSettings(rounded, backgroundColor, fontShadow, betterFont, bg, rounded , roundRadius)
    }
    companion object {
        var textColor = ColorSetting("TextColor", Color(255, 255, 255))
    }
}