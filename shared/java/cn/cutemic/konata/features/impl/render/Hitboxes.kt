package cn.cutemic.konata.features.impl.render

import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventRender3D
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.wrapper.mods.WrapperHitboxes
import java.awt.Color

class Hitboxes : Module("HitBoxes", Category.RENDER) {
    var color = ColorSetting("Color", Color(255, 255, 255, 255))

    init {
        addSettings(color)
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    @Subscribe
    fun onRender(event: EventRender3D?) {
        WrapperHitboxes.render(event, color)
    }

    companion object {
        var using = false
    }
}
