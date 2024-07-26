package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category

class PlayerDisplay : InterfaceModule("PlayerDisplay", Category.Interface) {
    init {
        addSettings(betterFont, rounded, fontShadow, backgroundColor, bg, rounded , roundRadius)
    }
}
