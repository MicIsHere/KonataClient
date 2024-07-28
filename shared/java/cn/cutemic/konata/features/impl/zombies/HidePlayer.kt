package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BindSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard


class HidePlayer : InterfaceModule("HidePlayer", Category.Zombies) {

    private var hideRadius = NumberSetting("HideRadius", 2, 1, 10, 0.1)

    private var isHide = false

    init {
        addSettings(keyBind, hideRadius)
    }

    @SubscribeEvent
    fun onRenderLiving(event: RenderLivingEvent.Pre<EntityLivingBase>) {
        if (this.isEnabled && event.entity is EntityPlayer && (event.entity != mc.thePlayer) && (event.entity.getDistanceSqToEntity(mc.thePlayer) < hideRadius.value.toFloat())
        ) {
            event.isCanceled = true

            if (!isHide){
                isHide = true
                NotificationManager.addNotification(Konata.i18n["hideplayer"],Konata.i18n["hideplayer.message.hidedplayer"],2f)
            }

        } else if (isHide) {
            NotificationManager.addNotification(Konata.i18n["hideplayer"],Konata.i18n["hideplayer.message.nothidedplayer"],2f)
        }
    }

    companion object {
        var keyBind = BindSetting("Key", Keyboard.KEY_NONE)
    }
}
