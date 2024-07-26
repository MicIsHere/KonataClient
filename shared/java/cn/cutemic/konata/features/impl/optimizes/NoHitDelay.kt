package cn.cutemic.konata.features.impl.optimizes

import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.interfaces.ProviderManager

class NoHitDelay : Module("NoHitDelay", Category.OPTIMIZE) {
    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    @Subscribe
    fun onTick(e: EventTick){
        ProviderManager.mcProvider.removeClickDelay()
    }

    companion object {
        var using = false
    }
}
