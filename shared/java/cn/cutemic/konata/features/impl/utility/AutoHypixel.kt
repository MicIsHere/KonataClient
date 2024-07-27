package cn.cutemic.konata.features.impl.utility

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.gameevent.TickEvent


class AutoHypixel : Module("AutoHypixel", Category.Cheat) {
    private var lobbyCheck = BooleanSetting("LobbyCheck", true)
    private var gameCheck = BooleanSetting("GameCheck", true)

    @JvmField
    var isLobby = false

    init {
        addSettings(lobbyCheck, gameCheck)
    }

    @Subscribe
    fun onTick(e: EventTick){
        if (lobbyCheck.value){

            if (isHypixelLobby()){
                isLobby = true
                NotificationManager.addNotification(Konata.i18n["autohypixel"],Konata.i18n["autohypixel.message.isinlobby"],2f)
            } else {
                isLobby = false
                NotificationManager.addNotification(Konata.i18n["autohypixel"],Konata.i18n["autohypixel.message.notinlobby"],2f)
            }

        }
    }

    private fun isHypixelLobby(): Boolean {
        if (!lobbyCheck.value) {
            return false
        }
        val strings = arrayOf("CLICK TO PLAY", "点击开始游戏")

        for (entity in Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity.name.startsWith("§e§l")) {
                for (string in strings) {
                    if (entity.name == "§e§l$string") {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    companion object {
        var using = false
    }
}
