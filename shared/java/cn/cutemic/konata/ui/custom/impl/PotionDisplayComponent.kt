package cn.cutemic.konata.ui.custom.impl

import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.PotionDisplay
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.interfaces.ProviderManager
import java.awt.Color
import kotlin.math.max

class PotionDisplayComponent : Component(PotionDisplay::class.java) {
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        var dY = y
        GlStateManager.pushMatrix()
        for (effect in ProviderManager.mcProvider.getPlayer()!!.activePotionEffects) {
            val title = I18n.format(effect.effectName) + " lv." + (effect.amplifier + 1)
            val duration = (effect.duration / 20 / 60).toString() + "min" + effect.duration / 20 % 60 + "s"
            val s18 = Konata.fontManager.s18
            val s16 = Konata.fontManager.s16
            val width = max(getStringWidth(s18, title), getStringWidth(s16, duration)) + 36
            drawRect(x, dY, width + 10, 32f, mod.backgroundColor.color)
            drawString(s18, title, x + 34, dY + 5, -1)
            drawString(s16, duration, x + 34, dY + 18, Color(200, 200, 200).rgb)
            // draw image
            val res = ResourceLocation("textures/gui/container/inventory.png")
            Utility.mc.textureManager.bindTexture(res)
            // get potion
            val potion = ProviderManager.utilityProvider.getPotionIconIndex(effect)
            // draw potion
            Gui.drawModalRectWithCustomSizedTexture(
                (x + 8).toInt(),
                (dY + 8).toInt(),
                (potion % 8 * 18).toFloat(),
                (198 + potion / 8 * 18).toFloat(),
                16,
                16,
                256f,
                256f
            )
            dY += 36f
            this.width = width + 12
        }
        GlStateManager.popMatrix()
        height = dY - y - 4
    }
}
