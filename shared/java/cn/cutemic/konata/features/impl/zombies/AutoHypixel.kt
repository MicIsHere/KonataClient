package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import net.minecraft.client.Minecraft


class AutoHypixel : Module("AutoHypixel", Category.Zombies) {
    private var lobbyCheck = BooleanSetting("LobbyCheck", true)
    private var zombiesCheck = BooleanSetting("ZombiesCheck", true)

    init {
        addSettings(lobbyCheck, zombiesCheck)
    }

    @Subscribe
    fun onTick(e: EventTick){
        if (isHypixelLobby() && !isLobby){
            isLobby = true
            NotificationManager.addNotification(Konata.i18n["autohypixel"],Konata.i18n["autohypixel.message.isinlobby"],2f)
        } else {
            isLobby = false
            if (isZombies() && !isZombies) {
                NotificationManager.addNotification(Konata.i18n["autohypixel"],Konata.i18n["autohypixel.message.isinzombies"],2f)
                isZombies = true
            } else {
                isZombies = false
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

    private fun isZombies(): Boolean {
        if (!zombiesCheck.value) {
            return false
        }
        val strings = arrayOf("Practice Dummy", "训练用僵尸")

        for (entity in Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity.name.startsWith("§a")) {
                for (string in strings) {
                    if (entity.name == "§a$string") {
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
        @JvmField
        var isLobby = false

        @JvmField
        var isZombies = false
    }
}
