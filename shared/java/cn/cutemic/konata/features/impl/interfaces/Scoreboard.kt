package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.BooleanSetting

class Scoreboard : InterfaceModule("Scoreboard", Category.Interface) {
    init {
        addSettings(rounded, backgroundColor, fontShadow, betterFont, score, bg, rounded , roundRadius)
    }

    override fun onEnable() {
        using = true
        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    companion object {
        @JvmField
        var using = false
        @JvmField
        var score = BooleanSetting("Score", false)
    }
}
