package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ModeSetting

class ArmorDisplay : InterfaceModule("ArmorDisplay", Category.Interface) {
    init {
        addSettings(rounded, backgroundColor, fontShadow, betterFont, mode, backgroundColor, bg, rounded , roundRadius)
    }

    companion object {
        @JvmField
        var mode = ModeSetting("Mode", 0, "SimpleHoriz", "SimpleVertical", "Vertical")
    }
}
