package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.math.MathTimer
import cn.cutemic.konata.websocket.client.WsClient
import cn.cutemic.konata.interfaces.ProviderManager

class IRC : Module("IRC", Category.Utility) {
    init {
        addSettings(showMates)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    private val timer = MathTimer()

    @Subscribe
    fun onTick(e: EventTick) {
        if (!timer.delay(5000))
            return
        if (ProviderManager.mcProvider.getWorld() == null)
            return
        if (Konata.INSTANCE.wsClient == null) {
            Konata.INSTANCE.wsClient = WsClient.start("wss://service.fpsmaster.top/")
            if (Konata.debug)
                Utility.sendClientMessage("尝试连接")
        }else if (Konata.INSTANCE.wsClient!!.isClosed && !Konata.INSTANCE.wsClient!!.isOpen){
            Konata.INSTANCE.wsClient!!.connect()
            if (Konata.debug)
                Utility.sendClientMessage("尝试连接")
        }
    }

    override fun onDisable() {
        super.onDisable()
        if (Konata.INSTANCE.wsClient != null && Konata.INSTANCE.wsClient!!.isOpen) {
            Konata.INSTANCE.wsClient!!.close()
        }
        using = false
    }

    companion object {
        var using: Boolean = false
        val showMates = BooleanSetting("showMates", true)

    }
}