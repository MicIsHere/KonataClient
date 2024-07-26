package cn.cutemic.konata.features.impl.render

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import java.awt.Color

class FireModifier : Module("FireModifier", Category.RENDER) {
    init {
        addSettings(height, customColor, colorSetting)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    companion object {
        @JvmField
        var using = false
        @JvmField
        var height = NumberSetting("Height", 0.5, 0, 0.7, 0.1)
        @JvmField
        var colorSetting = ColorSetting("Color", Color(255, 0, 0)){ customColor.value}
        @JvmField
        var customColor = BooleanSetting("CustomColor", false)
    }
}
