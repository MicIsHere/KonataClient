package cn.cutemic.konata.features.impl.optimizes

import org.lwjgl.input.Keyboard
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventUpdate
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BindSetting
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting

class SmoothZoom : Module("SmoothZoom", Category.OPTIMIZE) {
    private var zoomBind = BindSetting("ZoomBind", Keyboard.KEY_C)
    private var smoothMouse = BooleanSetting("SmoothMouse", false)

    init {
        addSettings(smoothCamera, speed, zoomBind, smoothMouse)
        this.set(true)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    @Subscribe
    fun onUpdate(e: EventUpdate?) {
        if (mc.currentScreen != null) return
        if (Keyboard.isKeyDown(zoomBind.value)) {
            zoom = true
            if (smoothMouse.value) mc.gameSettings.smoothCamera = true
        } else {
            zoom = false
            if (smoothMouse.value) mc.gameSettings.smoothCamera = false
        }
    }

    companion object {
        @JvmField
        var using = false

        @JvmField
        var zoom = false

        @JvmField
        var smoothCamera = BooleanSetting("smoothZoom", false)

        @JvmField
        val speed = NumberSetting("Speed", 4.0, 0.1, 10.0, 0.1) { smoothCamera.value }
    }
}
