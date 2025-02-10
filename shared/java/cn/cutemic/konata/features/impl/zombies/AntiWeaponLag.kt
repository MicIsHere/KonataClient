package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.event.events.EventUpdate
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ModeSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.zombies.ZombiesUtils
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemHoe
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.network.play.client.C0CPacketInput
import org.lwjgl.input.Mouse

class AntiWeaponLag : Module("AntiWeaponLag", Category.Zombies) {
    private var lagTick = NumberSetting("LagTick",6,1,40,1)
    private var notification = BooleanSetting("Notification", true)
    private var lagCount = 0

    init {
        addSettings(notification, lagTick)
    }

    override fun onEnable() {
        super.onEnable()
        lagCount = 0
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        lagCount = 0
        using = false
    }

    @Subscribe
    fun onTick(e: EventTick) {
        if (mc.thePlayer == null) {
            return
        }

        if (!ZombiesUtils.isWeapon(mc.thePlayer.heldItem.item)) {
            return
        }

        if (mc.thePlayer.heldItem.stackSize == 1 && mc.thePlayer.heldItem.itemDamage == 0) {
            if (lagCount == lagTick.value.toInt()) {
                mc.netHandler.addToSendQueue(C0APacketAnimation())
                if (notification.value) NotificationManager.addNotification(Konata.i18n["antiweaponlag"],Konata.i18n["antiweaponlag.message"],2f)
                lagCount = 0
                return
            }
            lagCount++
        }
    }

    companion object {
        var using = false
    }
}
