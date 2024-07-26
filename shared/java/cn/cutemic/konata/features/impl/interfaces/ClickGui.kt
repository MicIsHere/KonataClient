package cn.cutemic.konata.features.impl.interfaces

import org.lwjgl.input.Keyboard
import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.BindSetting

class ClickGui : InterfaceModule("ClickGui", Category.Interface) {
    init {
        addSettings(keyBind)
    }

    override fun onEnable() {
        super.onEnable()
        this.set(false)
    }


    companion object {
        var keyBind = BindSetting("Key", Keyboard.KEY_RSHIFT)
    }
}
