package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting

class CoordsDisplay : InterfaceModule("CoordsDisplay", Category.Interface) {
    val limitDisplay = BooleanSetting("LimitDisplay", false)
    val limitDisplayY = NumberSetting("LimitDisplayY", 92, 0, 255, 1)

    init {
        addSettings(FPSDisplay.textColor)
        addSettings(rounded, backgroundColor, fontShadow, betterFont, bg, rounded , roundRadius)
        addSettings(limitDisplay, limitDisplayY)
    }
}