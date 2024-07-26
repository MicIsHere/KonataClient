package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.BooleanSetting

class TabOverlay : InterfaceModule("TabOverlay", Category.Interface) {
    init {
        addSettings(showPing)
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
        var showPing = BooleanSetting("ShowPing", true)
    }
}
