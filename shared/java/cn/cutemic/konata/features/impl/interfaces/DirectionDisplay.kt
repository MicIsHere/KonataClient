package cn.cutemic.konata.features.impl.interfaces

import net.minecraft.client.gui.ScaledResolution
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventRender2D
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.ui.Compass
import cn.cutemic.konata.utils.Utility

class DirectionDisplay : Module("DirectionDisplay", Category.Interface) {
    val compass = Compass(325f, 325f, 1f, 2, true)

    @Subscribe
    fun onRender(e: EventRender2D){
        val scaledResolution = ScaledResolution(Utility.mc)
        compass.draw(scaledResolution)
    }
}
