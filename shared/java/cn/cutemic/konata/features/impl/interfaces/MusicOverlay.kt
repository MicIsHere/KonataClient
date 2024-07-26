package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import java.awt.Color

class MusicOverlay : InterfaceModule("MusicDisplay", Category.Interface) {
    init {
        addSettings(backgroundColor, progressColor, color, amplitude, bg, rounded , roundRadius)
    }

    companion object {
        var amplitude = NumberSetting("Amplitude", 10, 0, 10, 0.1)
        var progressColor = ColorSetting("ProgressColor", Color(255, 255, 255, 100))
        var color = ColorSetting("Visual", Color(255, 255, 255, 100))
    }
}
