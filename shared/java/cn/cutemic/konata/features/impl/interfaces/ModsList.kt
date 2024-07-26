package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class ModsList : InterfaceModule("ModsList", Category.Interface) {
    var showLogo = BooleanSetting("ShowLogo", true)
    var english = BooleanSetting("English", true)
    var rainbow = BooleanSetting("Rainbow", true)
    var color = ColorSetting("Color", Color(255, 255, 255)) { !rainbow.value }

    init {
        addSettings(showLogo, english, color, rainbow, betterFont, backgroundColor, bg)
    }
}