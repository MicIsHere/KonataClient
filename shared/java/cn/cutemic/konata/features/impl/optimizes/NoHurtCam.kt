package cn.cutemic.konata.features.impl.optimizes

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module

class NoHurtCam : Module("NoHurtCam", Category.OPTIMIZE) {
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
