package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.Konata
import cn.cutemic.konata.modules.account.AccountManager.Companion.skin
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.TextSetting
import cn.cutemic.konata.interfaces.ProviderManager

class SkinChanger : Module("SkinChanger", Category.Utility) {
    private var skinName = TextSetting("Skin", "")
    private var updateThread = Thread {
        while (true) {
            update()
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
    }

    init {
        addSettings(skinName)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
        if (ProviderManager.mcProvider.getPlayer() != null) {
            if (!updateThread.isAlive) {
                updateThread = Thread { update() }
                updateThread.start()
            }
        }
    }

    @Subscribe
    fun onTick(e: EventTick?) {
        if (ProviderManager.mcProvider.getPlayer() != null && ProviderManager.mcProvider.getPlayer()!!.ticksExisted % 30 == 0) {
            Konata.async.runnable { update() }
        }
        skin = skinName.value
    }

    fun update() {
        ProviderManager.skinProvider.updateSkin(
            ProviderManager.mcProvider.getPlayer()!!.name,
            ProviderManager.mcProvider.getPlayer()!!.uniqueID.toString(),
            skinName.value
        )
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    companion object {
        var using = false
    }
}
