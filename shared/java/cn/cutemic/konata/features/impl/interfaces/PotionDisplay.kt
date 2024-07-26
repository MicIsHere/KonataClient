package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category

class PotionDisplay : InterfaceModule("PotionDisplay", Category.Interface) {
    init {
        addSettings(rounded, backgroundColor, fontShadow, betterFont, bg, rounded , roundRadius)
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
    }
}
