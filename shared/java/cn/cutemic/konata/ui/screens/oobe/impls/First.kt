package cn.cutemic.konata.ui.screens.oobe.impls

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import cn.cutemic.konata.Konata
import cn.cutemic.konata.ui.screens.oobe.Scene
import cn.cutemic.konata.ui.common.GuiButton
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.awt.Desktop
import java.net.URI

class First : Scene() {
    var btn: GuiButton = GuiButton(Konata.i18n["oobe.first.next"]) {
        val url = "https://konata.cutemic.cn/tutorial"
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            try {
                desktop.browse(URI(url))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Konata.oobeScreen.nextScene()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        val sr = ScaledResolution(Minecraft.getMinecraft())
        Render2DUtils.drawRect(
            0f,
            0f,
            sr.scaledWidth.toFloat(),
            sr.scaledHeight.toFloat(),
            Color(235, 242, 255).rgb
        )
        Konata.fontManager.s36.drawCenteredString(
            Konata.i18n["oobe.first.desc"],
            sr.scaledWidth / 2f,
            sr.scaledHeight / 2f - 60,
            Konata.theme.textColorDescription.rgb
        )
        Konata.fontManager.s40.drawCenteredString(
            Konata.i18n["oobe.first.title"],
            sr.scaledWidth / 2f,
            sr.scaledHeight / 2f - 40,
            Konata.theme.primary.rgb
        )
        val skip = Konata.i18n["oobe.first.skip"]
        val skipFont = Konata.fontManager.s22
        val skipWidth = skipFont.getStringWidth(skip)
        val x = sr.scaledWidth - 10 - skipWidth
        val skipY = sr.scaledHeight - 20
        skipFont.drawString(skip, x.toFloat(), skipY, Konata.theme.textColorDescription.rgb)
        btn.render(sr.scaledWidth / 2f - 30, sr.scaledHeight / 2f + 40, 60f, 24f, mouseX.toFloat(), mouseY.toFloat())
    }

    override fun mouseClick(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClick(mouseX, mouseY, mouseButton)
        val sr = ScaledResolution(Minecraft.getMinecraft())
        btn.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        val skipFont = Konata.fontManager.s22
        val skip = Konata.i18n["oobe.first.skip"]
        val skipWidth = skipFont.getStringWidth(skip)
        val x = sr.scaledWidth - 10 - skipWidth
        val skipY = sr.scaledHeight - 20
        if (Render2DUtils.isHovered(
                x.toFloat(),
                skipY.toFloat(),
                skipWidth.toFloat(),
                20f,
                mouseX,
                mouseY
            ) && Mouse.isButtonDown(0)
        ) {
            Konata.oobeScreen.nextScene()
        }
    }
}
