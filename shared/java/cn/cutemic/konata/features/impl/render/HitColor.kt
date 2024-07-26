package cn.cutemic.konata.features.impl.render

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.ColorSetting
import java.awt.Color

class HitColor : Module("HitColor", Category.RENDER) {
    init {
        addSettings(color)
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
        var color = ColorSetting("Color", Color(255, 0, 0, 120))
        @JvmField
        var using = false
        @JvmStatic
        val red: Float
            get() = color.color.red / 255f
        @JvmStatic
        val green: Float
            get() = color.color.green / 255f
        @JvmStatic
        val blue: Float
            get() = color.color.blue / 255f
        @JvmStatic
        val alpha: Float
            get() = color.color.alpha / 255f
    }
}
