package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventPacket
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.interfaces.ProviderManager

class TimeChanger : Module("TimeChanger", Category.Utility) {
    var time = NumberSetting("Time", 0, 0, 24000, 1)

    init {
        addSettings(time)
    }

    @Subscribe
    fun onTick(e: EventTick?) {
        if (ProviderManager.worldClientProvider.getWorld() != null) ProviderManager.worldClientProvider.setWorldTime(time.value.toLong())
    }

    @Subscribe
    fun onPacket(e: EventPacket) {
        if (e.type === EventPacket.PacketType.RECEIVE) {
            if (ProviderManager.packetTimeUpdate.isPacket(e.packet)) e.cancel()
        }
    }
}
