package cn.cutemic.konata.features.impl.render

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module

class FullBright : Module("FullBright", Category.RENDER) {
    private var oldGamma = 0f
    override fun onEnable() {
        super.onEnable()
        oldGamma = mc.gameSettings.gammaSetting
        mc.gameSettings.gammaSetting = 100f
    }

    override fun onDisable() {
        super.onDisable()
        mc.gameSettings.gammaSetting = oldGamma
    }
}
