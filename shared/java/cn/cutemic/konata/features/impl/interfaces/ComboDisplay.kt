package cn.cutemic.konata.features.impl.interfaces

import net.minecraft.entity.Entity
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventAttack
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.interfaces.ProviderManager
import java.awt.Color

class ComboDisplay : InterfaceModule("ComboDisplay", Category.Interface) {
    var target: Entity? = null

    init {
        addSettings(textColor, backgroundColor, betterFont, fontShadow, rounded, bg, rounded , roundRadius)
    }

    @Subscribe
    fun onTick(e: EventTick) {
        if (ProviderManager.mcProvider.getPlayer() == null) return
        if (ProviderManager.mcProvider.getPlayer()!!.hurtTime == 1 || target != null && ProviderManager.utilityProvider.getDistanceToEntity(
                ProviderManager.mcProvider.getPlayer(),
                target
            ) > 5
        ) {
            combo = 0
        }
        if (target != null && target!!.isEntityAlive && target!!.hurtResistantTime == 19) {
            combo++
        }
    }

    @Subscribe
    fun attack(e: EventAttack) {
        target = e.target
    }

    companion object {
        @JvmField
        var combo = 0
        @JvmField
        var textColor = ColorSetting("TextColor", Color(255, 255, 255))
    }
}
