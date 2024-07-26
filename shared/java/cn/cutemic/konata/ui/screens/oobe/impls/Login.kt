package cn.cutemic.konata.ui.screens.oobe.impls

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import cn.cutemic.konata.Konata
import cn.cutemic.konata.modules.account.AccountManager.Companion.login
import cn.cutemic.konata.ui.common.TextField
import cn.cutemic.konata.ui.screens.oobe.Scene
import cn.cutemic.konata.ui.common.GuiButton
import cn.cutemic.konata.ui.screens.mainmenu.MainMenu
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.math.animation.Type
import cn.cutemic.konata.utils.os.FileUtils.saveTempValue
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.awt.Desktop
import java.net.URI

class Login(isOOBE: Boolean) : Scene() {
    private var msg: String? = null
    private var msgbox = false
    var btn: GuiButton
    private var btn2: GuiButton
    private var username: TextField =
        TextField(
            Konata.fontManager.s18,
            false,
            Konata.i18n["oobe.login.username"],
            -1,
            Color(200, 200, 200).rgb,
            32
        )
    private var password: TextField =
        TextField(
            Konata.fontManager.s18,
            true,
            Konata.i18n["oobe.login.password"],
            -1,
            Color(200, 200, 200).rgb,
            32
        )
    private var msgBoxAnimation = ColorAnimation(Color(0, 0, 0, 0))

    init {
        username.text = Konata.configManager.configure.getOrCreate("username", "")
        btn = GuiButton(Konata.i18n["oobe.login.login"]) {
            try {
                val login = login(username.text, password.text)
                if (login["code"].asInt == 200) {
                    if (Konata.accountManager != null) {
                        Konata.accountManager.username = username.text
                        Konata.accountManager.token = login["msg"].asString
                    }
                    saveTempValue("token", Konata.accountManager.token)
                    Konata.INSTANCE.loggedIn = true
                    if (isOOBE) {
                        Konata.oobeScreen.nextScene()
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(MainMenu())
                    }
                } else {
                    msg = login["msg"].asString
                    msgbox = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                msg = "未知错误: " + e.message
                msgbox = true
            }
        }
        btn2 = GuiButton(Konata.i18n["oobe.login.skip"]) {
            if (isOOBE) {
                Konata.oobeScreen.nextScene()
            } else {
                Minecraft.getMinecraft().displayGuiScreen(MainMenu())
            }
            Konata.configManager.configure["username"] = "offline"
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        val sr = ScaledResolution(Minecraft.getMinecraft())
//        GradientUtils.drawGradient(
//            0f,
//            0f,
//            sr.scaledWidth.toFloat(),
//            sr.scaledHeight.toFloat(),
//            1f,
//            Color(255, 255, 255),
//            Color(235, 242, 255),
//            Color(255, 255, 255),
//            Color(235, 242, 255)
//        )
        Render2DUtils.drawRect(
            0f,
            0f,
            sr.scaledWidth.toFloat(),
            sr.scaledHeight.toFloat(),
            Color(235, 242, 255).rgb
        )
//        GlStateManager.pushMatrix()
//        GlStateManager.enableBlend()
        Konata.fontManager.s24.drawCenteredString(
            Konata.i18n["oobe.login.desc"],
            sr.scaledWidth / 2f,
            sr.scaledHeight / 2f - 90,
            Konata.theme.textColorDescription.rgb
        )
        Konata.fontManager.s18.drawString(
            Konata.i18n["oobe.login.register"],
            sr.scaledWidth / 2f - 90,
            sr.scaledHeight / 2f + 15,
            Konata.theme.primary.rgb
        )
        Konata.fontManager.s40.drawCenteredString(
            Konata.i18n["oobe.login.title"],
            sr.scaledWidth / 2f,
            sr.scaledHeight / 2f - 75,
            Konata.theme.primary.rgb
        )
        btn.render(sr.scaledWidth / 2f - 70, sr.scaledHeight / 2f + 40, 60f, 24f, mouseX.toFloat(), mouseY.toFloat())
        btn2.render(sr.scaledWidth / 2f + 5, sr.scaledHeight / 2f + 40, 60f, 24f, mouseX.toFloat(), mouseY.toFloat())
        username.drawTextBox(sr.scaledWidth / 2f - 90, sr.scaledHeight / 2f - 40, 180f, 20f)
        password.drawTextBox(sr.scaledWidth / 2f - 90, sr.scaledHeight / 2f - 10, 180f, 20f)
        msgBoxAnimation.update()
        if (msgbox) {
            msgBoxAnimation.start(Color(0, 0, 0, 0), Color(0, 0, 0, 100), 0.6f, Type.EASE_IN_OUT_QUAD)
            Render2DUtils.drawRect(0f, 0f, sr.scaledWidth.toFloat(), sr.scaledHeight.toFloat(), msgBoxAnimation.color)
            Render2DUtils.drawOptimizedRoundedRect(
                sr.scaledWidth / 2f - 100,
                sr.scaledHeight / 2f - 50,
                200f,
                100f,
                Color(255, 255, 255)
            )
            Render2DUtils.drawOptimizedRoundedRect(
                sr.scaledWidth / 2f - 100,
                sr.scaledHeight / 2f - 50,
                200f,
                20f,
                Color(113, 127, 254)
            )
            Konata.fontManager.s18.drawString(
                Konata.i18n["oobe.login.info"],
                sr.scaledWidth / 2f - 90,
                sr.scaledHeight / 2f - 45,
                -1
            )
            Konata.fontManager.s18.drawString(
                msg,
                sr.scaledWidth / 2f - 90,
                sr.scaledHeight / 2f - 20,
                Color(60, 60, 60).rgb
            )
        } else {
            msgBoxAnimation.start(Color(0, 0, 0, 100), Color(0, 0, 0, 0), 0.6f, Type.EASE_IN_OUT_QUAD)
        }
//        GlStateManager.disableBlend()
//        GlStateManager.popMatrix()
    }

    override fun mouseClick(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClick(mouseX, mouseY, mouseButton)
        btn.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        btn2.mouseClick(mouseX.toFloat(), mouseY.toFloat(), mouseButton)
        username.mouseClicked(mouseX, mouseY, mouseButton)
        password.mouseClicked(mouseX, mouseY, mouseButton)
        val sr = ScaledResolution(Minecraft.getMinecraft())
        if (Render2DUtils.isHovered(
                sr.scaledWidth / 2f - 90,
                sr.scaledHeight / 2f + 15,
                100f,
                10f,
                mouseX,
                mouseY
            ) && mouseButton == 0
        ) {
            val url = "https://konata.cutemic.cn/register"
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                try {
                    desktop.browse(URI(url))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (msgbox && msgBoxAnimation.color.alpha > 50) msgbox = false
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        username.textboxKeyTyped(typedChar, keyCode)
        Konata.configManager.configure["username"] = username.text
        password.textboxKeyTyped(typedChar, keyCode)
    }
}
