package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventUpdate
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.interfaces.ProviderManager

class Sprint : Module("Sprint", Category.Utility) {
    @Subscribe
    fun onUpdate(e: EventUpdate) {
        ProviderManager.gameSettings.setKeyPress(mc.gameSettings.keyBindSprint, true)
    }

    override fun onDisable() {
        super.onDisable()
        ProviderManager.gameSettings.setKeyPress(mc.gameSettings.keyBindSprint, false)
    }
}
