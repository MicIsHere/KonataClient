package cn.cutemic.konata.features.impl

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import java.awt.Color

open class InterfaceModule(name: String, category: Category) : Module(name, category) {
    @JvmField
    var rounded = BooleanSetting("Round", true)

    @JvmField
    var roundRadius = NumberSetting("RoundRadius", 3, 0, 30, 1) { rounded.value }

    @JvmField
    var betterFont = BooleanSetting("BetterFont", false)

    @JvmField
    var fontShadow = BooleanSetting("FontShadow", true) { betterFont.value }

    @JvmField
    var bg = BooleanSetting("Background", true)

    @JvmField
    var backgroundColor = ColorSetting("BackgroundColor", Color(0, 0, 0, 0)) { bg.value }
}
