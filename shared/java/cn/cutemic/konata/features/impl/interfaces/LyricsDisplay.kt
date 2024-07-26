package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class LyricsDisplay : InterfaceModule("LyricsDisplay", Category.Interface) {
    init {
        addSettings(backgroundColor, rounded, betterFont, textColor, textBG, bg, rounded , roundRadius)
    }

    companion object {
        @JvmField
        var textColor = ColorSetting("TextColor", Color(255, 255, 255))
        @JvmField
        var textBG = ColorSetting("TextColorBG", Color(255, 255, 255))
    }
}
