package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.TextSetting

class ClientCommand : Module("ClientCommand", Category.Utility) {
    init {
        addSettings(prefix)
    }

    override fun onEnable() {
        using = true
        super.onEnable()
    }

    override fun onDisable() {
        using = false
        super.onDisable()
    }

    companion object {
        var using = false
        val prefix = TextSetting("prefix", "#")
    }
}