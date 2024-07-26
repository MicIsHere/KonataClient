package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ModeSetting

class LevelTag : Module("Nametags", Category.Utility) {
    init {
        addSettings(showSelf, health)
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
        var showSelf = BooleanSetting("ShowSelf", true)
        @JvmField
        var health = BooleanSetting("Health", true)
        var levelMode = ModeSetting("RankMode", 0, "None", "Bedwars", "Bedwars-xp", "Skywars", "Kit")
        @JvmField
        var using = false
    }
}
