package cn.cutemic.konata.features.impl.render

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module

class MinimizedBobbing : Module("MinimizedBobbing", Category.RENDER) {
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
