package cn.cutemic.konata.ui.custom

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import cn.cutemic.konata.Konata
import cn.cutemic.konata.font.impl.UFontRenderer
import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.ui.click.MainPanel
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.render.Render2DUtils
import cn.cutemic.konata.interfaces.ProviderManager
import cn.cutemic.konata.utils.render.shader.BlurBuffer
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

open class Component(clazz: Class<*>?) {

    private var dragX = 0f
    private var dragY = 0f

    @JvmField
    var mod: InterfaceModule = Konata.moduleManager.getModule(clazz!!) as InterfaceModule
    var position = Position.LT

    @JvmField
    var x = 0f

    @JvmField
    var y = 0f

    @JvmField
    var width = 0f

    @JvmField
    var height = 0f

    open fun draw(x: Float, y: Float) {}
    var alpha = 0f

    fun shouldDisplay(): Boolean {
        return mod.isEnabled
    }

    fun getRealPosition(): FloatArray {
        val sr = ScaledResolution(Minecraft.getMinecraft())
        var rX = 0f
        var rY = 0f
        x = max(0f, min(1f, x))
        y = max(0f, min(1f, y))
        when (position) {
            Position.LT -> {
                rX = x * sr.scaledWidth / 2f
                rY = y * sr.scaledHeight / 2f
            }

            Position.RT -> {
                rX = sr.scaledWidth - (x * sr.scaledWidth / 2f + width)
                rY = y * sr.scaledHeight / 2f
            }

            Position.LB -> {
                rX = x * sr.scaledWidth / 2f
                rY = sr.scaledHeight - (y * sr.scaledHeight / 2f + height)
            }

            Position.RB -> {
                rX = sr.scaledWidth - (x * sr.scaledWidth / 2f + width)
                rY = sr.scaledHeight - (y * sr.scaledHeight / 2f + height)
            }

            Position.CT -> {

            }
        }
        return floatArrayOf(rX, rY)
    }

    fun display(mouseX: Int, mouseY: Int) {
        val rX = getRealPosition()[0]
        val rY = getRealPosition()[1]
        draw(rX.toInt().toFloat(), rY.toInt().toFloat())
        if (Utility.mc.currentScreen !is GuiChat && !(Utility.mc.currentScreen is MainPanel && !Render2DUtils.isHovered(
                MainPanel.x.toFloat(),
                MainPanel.y.toFloat(),
                MainPanel.width,
                MainPanel.height,
                mouseX,
                mouseY
            ))
        ) return
        val drag = Konata.componentsManager.dragLock == mod.name
        alpha = if (Render2DUtils.isHovered(rX, rY, width, height, mouseX, mouseY) || drag) {
            if ((base(alpha.toDouble(), 50.0, 0.1).toFloat()).isNaN())
                0f else base(alpha.toDouble(), 50.0, 0.1).toFloat()
        } else {
            if ((base(alpha.toDouble(), 0.0, 0.1).toFloat()).isNaN())
                0f else base(alpha.toDouble(), 0.0, 0.1).toFloat()
        }
        Render2DUtils.drawOptimizedRoundedRect(rX - 2, rY - 2, width + 4, height + 4, Color(0, 0, 0, alpha.toInt()))
        if (!Mouse.isButtonDown(0)) {
            Konata.componentsManager.dragLock = ""
        }
        if (Render2DUtils.isHovered(rX, rY, width, height, mouseX, mouseY) || drag) {
            Konata.fontManager.s14.drawString(
                Konata.i18n[mod.name.lowercase()],
                rX,
                rY - 10,
                -1
            )
            if (!Mouse.isButtonDown(0)) return
            if (!drag && Konata.componentsManager.dragLock.isEmpty()) {
                dragX = mouseX - rX
                dragY = mouseY - rY
                Konata.componentsManager.dragLock = mod.name
            }
            if (Konata.componentsManager.dragLock == mod.name) {
                move(mouseX.toFloat(), mouseY.toFloat())
                Konata.componentsManager.dragLock = mod.name
            }
        }
    }

    private fun move(x: Float, y: Float) {
        val sr = ScaledResolution(Utility.mc)
        var changeX = 0f
        var changeY = 0f
        if (x > sr.scaledWidth / 2f) {
            if (y >= sr.scaledHeight / 2f) position = Position.RB else if (y < sr.scaledHeight / 2f) position =
                Position.RT
        } else {
            if (y >= sr.scaledHeight / 2f) position = Position.LB else if (y < sr.scaledHeight / 2f) position =
                Position.LT
        }
        when (position) {
            Position.LT -> {
                changeX = x - dragX
                changeY = y - dragY
            }

            Position.RT -> {
                changeX = sr.scaledWidth - x - width + dragX
                changeY = y - dragY
            }

            Position.LB -> {
                changeX = x - dragX
                changeY = sr.scaledHeight - y - height + dragY
            }

            Position.RB -> {
                changeX = sr.scaledWidth - x - width + dragX
                changeY = sr.scaledHeight - y - height + dragY
            }

            Position.CT -> {

            }
        }
        this.x = changeX / sr.scaledWidth * 2f
        this.y = changeY / sr.scaledHeight * 2f
    }

    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color?) {
        BlurBuffer.blurArea(x, y, width, height, true)
        if (mod.bg.value)
            if (mod.rounded.value) {
                Render2DUtils.drawOptimizedRoundedRect(x, y, width, height, mod.roundRadius.value.toInt(), color!!.rgb)
            } else {
                Render2DUtils.drawRect(x, y, width, height, color)
            }
    }

    fun drawString(font: UFontRenderer, text: String, x: Float, y: Float, color: Int) {
        if (mod.betterFont.value) {
            if (mod.fontShadow.value) font.drawStringWithShadow(text, x, y, color) else font.drawString(
                text,
                x,
                y,
                color
            )
        } else {
            if (mod.fontShadow.value) ProviderManager.mcProvider.getFontRenderer()
                .drawStringWithShadow(text, x, y, color) else ProviderManager.mcProvider.drawString(text, x, y, color)
        }
    }

    protected fun getStringWidth(font: UFontRenderer, name: String): Float {
        return if (mod.betterFont.value) {
            font.getStringWidth(name).toFloat()
        } else {
            ProviderManager.mcProvider.getFontRenderer().getStringWidth(name).toFloat()
        }
    }
}
