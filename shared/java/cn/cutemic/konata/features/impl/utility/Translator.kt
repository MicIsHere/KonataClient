package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module

class Translator : Module("Translator", Category.Utility) {
    companion object{
        var using = false
    }
    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }
}