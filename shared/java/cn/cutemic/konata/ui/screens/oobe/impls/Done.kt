package cn.cutemic.konata.ui.screens.oobe.impls

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import cn.cutemic.konata.Konata
import cn.cutemic.konata.ui.screens.oobe.Scene
import cn.cutemic.konata.ui.common.GuiButton
import cn.cutemic.konata.ui.screens.mainmenu.MainMenu
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color

class Done : Scene() {
    var btn: GuiButton = GuiButton(Konata.i18n["oobe.done.start"]) {
        Konata.configManager.configure["firstStart"] = "false"
        Minecraft.getMinecraft().displayGuiScreen(MainMenu())
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
            Konata.i18n["oobe.done.desc"],
            sr.scaledWidth / 2f,
            70f,
            Konata.theme.textColorDescription.rgb
        )
        Konata.fontManager.s40.drawCenteredString(
            Konata.i18n["oobe.done.title"],
            sr.scaledWidth / 2f,
            100f,
            Konata.theme.primary.rgb
        )
        btn.render(sr.scaledWidth / 2f - 30, sr.scaledHeight / 2f + 40, 60f, 24f, mouseX.toFloat(), mouseY.toFloat())
    }

    override fun mouseClick(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClick(mouseX, mouseY, mouseButton)
        btn.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
    }
}
