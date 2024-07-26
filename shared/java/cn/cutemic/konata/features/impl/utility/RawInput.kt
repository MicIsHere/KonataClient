package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.utils.thirdparty.rawinput.RawInputMod

class RawInput : Module("RawInput", Category.Utility) {
    private var rawInputMod = RawInputMod()
    override fun onEnable() {
        super.onEnable()
        rawInputMod.start()
    }

    override fun onDisable() {
        super.onDisable()
        rawInputMod.stop()
    }
}
