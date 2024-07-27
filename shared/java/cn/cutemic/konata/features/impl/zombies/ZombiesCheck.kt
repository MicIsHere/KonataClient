package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.gameevent.TickEvent


class ZombiesCheck : Module("ZombiesCheck", Category.Zombies) {
    private var mapCheck = BooleanSetting("MapCheck", true)
    private var lobbyCheck = BooleanSetting("LobbyCheck", true)
    private var gameCheck = BooleanSetting("GameCheck", true)

    @JvmField
    var isLobby = false

    init {
        addSettings(mapCheck, lobbyCheck, gameCheck)
    }

    @Subscribe
    fun onTick(e: TickEvent){
        if (lobbyCheck.value){

            if (isHypixelLobby()){
                isLobby = true
                NotificationManager.addNotification(Konata.i18n["zombiescheck"],Konata.i18n["zombiescheck.message.isinlobby"],2f)
            } else {
                isLobby = false
                NotificationManager.addNotification(Konata.i18n["zombiescheck"],Konata.i18n["zombiescheck.message.notinlobby"],2f)
            }

        }
    }

    private fun isHypixelLobby(): Boolean {
        if (!lobbyCheck.value) {
            return false
        }
        val strings = arrayOf("CLICK TO PLAY", "点击开始游戏")

        for (entity in mc.theWorld.loadedEntityList) {
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
