package cn.cutemic.konata.ui.custom.impl

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import cn.cutemic.konata.features.impl.interfaces.MiniMap
import cn.cutemic.konata.interfaces.ProviderManager
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.ui.minimap.XaeroMinimap
import cn.cutemic.konata.ui.minimap.animation.MinimapAnimation
import cn.cutemic.konata.ui.minimap.interfaces.InterfaceHandler
import cn.cutemic.konata.utils.render.Render2DUtils
import java.io.IOException

class MiniMapComponent : Component(MiniMap::class.java) {
    init {
        y = 0.3f
        width = 75f
        height = 75f
    }

    private var loadedMinimap = false
    private val minimap: XaeroMinimap = XaeroMinimap()

    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        if (!loadedMinimap) {
            loadedMinimap = true
            try {
                minimap.load()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Render2DUtils.drawImage(
            ResourceLocation("client/gui/minimapbg.png"),
            x + width / 2 - 179 / 4f,
            y + width / 2 - 179 / 4f,
            179f / 2f,
            179f / 2f,
            -1
        )
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

        InterfaceHandler.drawInterfaces(ProviderManager.timerProvider.getRenderPartialTicks())
        Render2DUtils.drawRect(x + width / 2 - 1, y + height / 2 - 1, 2f, 2f, -1)
        MinimapAnimation.tick()
    }
}