package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category

class InventoryDisplay : InterfaceModule("InventoryDisplay", Category.Interface) {
    init {
        addSettings(rounded, backgroundColor, bg, rounded , roundRadius)
    }
}
