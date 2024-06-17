package top.fpsmaster.ui.screens.oobe.impls

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import top.fpsmaster.FPSMaster
import top.fpsmaster.ui.screens.oobe.Scene
import top.fpsmaster.ui.common.GuiButton
import top.fpsmaster.ui.screens.mainmenu.MainMenu
import top.fpsmaster.utils.render.Render2DUtils
import top.fpsmaster.wrapper.GuiMainMenuProvider
import java.awt.Color

class Done : Scene() {
    var btn: GuiButton

    init {
        btn = GuiButton(FPSMaster.INSTANCE.i18n["oobe.done.start"]) {
            FPSMaster.INSTANCE.configManager.configure["firstStart"] = "false"
            Minecraft.getMinecraft().displayGuiScreen(MainMenu())
        }
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
        FPSMaster.INSTANCE.fontManager!!.s36.drawCenteredString(
            FPSMaster.INSTANCE.i18n["oobe.done.desc"],
            sr.scaledWidth / 2f,
            70f,
            FPSMaster.theme.getTextColorDescription().rgb
        )
        FPSMaster.INSTANCE.fontManager!!.s40.drawCenteredString(
            FPSMaster.INSTANCE.i18n["oobe.done.title"],
            sr.scaledWidth / 2f,
            100f,
            FPSMaster.theme.getPrimary().rgb
        )
        btn.render(sr.scaledWidth / 2f - 30, sr.scaledHeight / 2f + 40, 60f, 24f, mouseX.toFloat(), mouseY.toFloat())
    }

    override fun mouseClick(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClick(mouseX, mouseY, mouseButton)
        btn.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
    }
}
