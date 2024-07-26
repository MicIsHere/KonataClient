package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category

class BetterChat : InterfaceModule("BetterChat", Category.Interface) {
    init {
        addSettings(backgroundColor, fontShadow, betterFont, bg)
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
