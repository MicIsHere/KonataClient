package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting

class CustomFOV : Module("CustomFov", Category.Utility) {
    init {
        addSettings(noSpeedFov, noFlyFov, noBowFov)
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
        var noSpeedFov = BooleanSetting("NoSpeedFov", false)

        @JvmField
        var noFlyFov = BooleanSetting("NoFlyFov", false)

        @JvmField
        var noBowFov = BooleanSetting("NoBowFov", false)

        @JvmField
        var using = false
    }
}
