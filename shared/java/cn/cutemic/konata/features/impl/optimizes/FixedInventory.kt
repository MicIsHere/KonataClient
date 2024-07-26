package cn.cutemic.konata.features.impl.optimizes

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module

class FixedInventory : Module("FixedInventory", Category.OPTIMIZE) {
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
