package cn.cutemic.konata.ui.click

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.ui.click.component.ScrollContainer
import cn.cutemic.konata.ui.click.music.MusicPanel
import cn.cutemic.konata.ui.click.music.MusicPanel.draw
import cn.cutemic.konata.ui.click.ornaments.OrnamentPanel
import cn.cutemic.konata.ui.click.modules.ModuleRenderer
import cn.cutemic.konata.ui.click.themes.DarkTheme
import cn.cutemic.konata.ui.click.themes.LightTheme
import cn.cutemic.konata.utils.math.animation.Animation
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.math.animation.Type
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.io.IOException
import java.util.*
import kotlin.math.max
import kotlin.math.min

class MainPanel(private val doesGuiPauseGame: Boolean) : GuiScreen() {
    private var drag = false
    private var dragX = 0f
    private var dragY = 0f
    private var curType = Category.OPTIMIZE
    private var categories = LinkedList<CategoryComponent>()
    private val leftWidth = 110f
    private var modsWheel = 0f
    private var wheelTemp = 0f
    private var sizeDrag = false
    private var sizeDragX = 0f
    private var sizeDragY = 0f

    // window animation
    private var scaleAnimation = Animation()

    // selection
    private var selection = 0f

    // color animation
    private var sizeDragBorder = ColorAnimation(255, 255, 255, 0)
    private var backgroundColor = ColorAnimation(Konata.theme.background)
    private var modeColor = ColorAnimation(Konata.theme.typeSelectionBackground)
    private var logoColor = ColorAnimation(Konata.theme.logo)



    private var close = false

    // module list animation
    private var moduleListAlpha = 0f
    private val mods = LinkedList<ModuleRenderer>()
    private var modHeight = 0f
    private val modsContainer = ScrollContainer()


    override fun doesGuiPauseGame(): Boolean {
        return doesGuiPauseGame
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        var mouseY = mouseY
        super.drawScreen(mouseX, mouseY, partialTicks)
        val sr = ScaledResolution(mc)
        GL11.glPushMatrix()
        if (!Mouse.isButtonDown(0)) {
            dragLock = "null"
            drag = false
            sizeDrag = false
        }
        if (drag) {
            x = (mouseX - dragX).toInt()
            mouseY -= dragY.toInt()
            y = mouseY
        }
        x = max(min(x, (sr.scaledWidth - Companion.width - 5).toInt()), 5)
        y = max(min(y, (sr.scaledHeight - Companion.height - 5).toInt()), 5)
        val w: Float = min(mouseX + sizeDragX, sr.scaledWidth - 5f) - x
        val h: Float = min(mouseY + sizeDragY, sr.scaledHeight - 5f) - y
        if (sizeDrag && (Companion.width > 400 || w > Companion.width)) {
            Companion.width = w
        }
        if (sizeDrag && (Companion.height > 240 || h > Companion.height)) {
            Companion.height = h
        }
        Companion.width = max(min(Companion.width, sr.scaledWidth - 10f), 400f)
        Companion.height = max(min(Companion.height, sr.scaledHeight - 10f), 220f)
        if (close) {
            if (scaleAnimation.value <= 0.5) {
                mc.displayGuiScreen(null)
                if (mc.currentScreen == null) {
                    mc.setIngameFocus()
                }
            }
        } else {
            scaleAnimation.start(scaleAnimation.value, 1.0, 0.2f, Type.EASE_OUT_BACK)
        }
        scaleAnimation.update()
        GlStateManager.translate(sr.scaledWidth / 2.0, sr.scaledHeight / 2.0, 0.0)
        GL11.glScaled(scaleAnimation.value, scaleAnimation.value, 1.0)
        GlStateManager.translate(-sr.scaledWidth / 2.0, -sr.scaledHeight / 2.0, 0.0)

        //绘制主窗体
        Render2DUtils.drawOptimizedRoundedRect(
            (x - 1).toFloat(),
            (y - 1).toFloat(),
            Companion.width + 2,
            Companion.height + 2,
            sizeDragBorder.color
        )
        backgroundColor.base(Konata.theme.background)
        Render2DUtils.drawOptimizedRoundedRect(
            x.toFloat(),
            y.toFloat(),
            Companion.width,
            Companion.height,
            backgroundColor.color
        )
        logoColor.base(Konata.theme.logo)

        Render2DUtils.drawImage(
            ResourceLocation("client/gui/settings/logo.png"),
            x + leftWidth / 2 - 40 - 5,
            (y + 15).toFloat(),
            81.5f,
            64 / 2f,
            logoColor.color
        )
        if (drag || sizeDrag) {
            sizeDragBorder.start(Color(255, 255, 255, 100), Color(255, 255, 255), 0.15f, Type.EASE_IN_OUT_QUAD)
        } else {
            sizeDragBorder.start(sizeDragBorder.color, Color(255, 255, 255, 0), 0.2f, Type.EASE_IN_OUT_QUAD)
        }
        sizeDragBorder.update()
        if (Render2DUtils.isHovered(x + Companion.width - 10, y + Companion.height - 10, 10f, 10f, mouseX, mouseY)) {
            Render2DUtils.drawImage(
                ResourceLocation("client/gui/settings/drag.png"),
                x + Companion.width - 5,
                y + Companion.height - 5,
                5f,
                5f,
                Konata.theme.dragHovered
            )
        } else {
            Render2DUtils.drawImage(
                ResourceLocation("client/gui/settings/drag.png"),
                x + Companion.width - 5,
                y + Companion.height - 5,
                5f,
                5f,
                Konata.theme.drag
            )
        }
        var my = (y + 60).toFloat()
        for (m in categories) {
            Render2DUtils.drawOptimizedRoundedRect(
                (x + 5).toFloat(),
                my - 6,
                leftWidth - 10,
                20f,
                m.categorySelectionColor.color
            )
            my += 24f
        }
        //绘制categories
        my = (y + 60).toFloat()
        Render2DUtils.drawOptimizedRoundedRect(
            (x + 5).toFloat(),
            selection - 6,
            leftWidth - 10,
            20f,
            Konata.theme.primary
        )
        for (m in categories) {
            if (Render2DUtils.isHovered(x.toFloat(), my - 6, leftWidth - 10, 20f, mouseX, mouseY)) {
                m.categorySelectionColor.base(Konata.theme.typeSelectionBackground)
            } else {
                m.categorySelectionColor.base(Render2DUtils.reAlpha(Konata.theme.typeSelectionBackground, 0))
            }
            if (m.category === curType) {
                selection = if (sizeDrag || drag) {
                    my
                } else {
                    base(selection.toDouble(), my.toDouble(), 0.2).toFloat()
                }
            }
            m.render(
                (x + 5).toFloat(),
                my,
                leftWidth - 10,
                20f,
                mouseX.toFloat(),
                mouseY.toFloat(),
                curType === m.category
            )
            my += 24f
        }

        // 绘制主题切换按钮
        Render2DUtils.drawOptimizedRoundedRect(
            (x + 40).toFloat(),
            y + Companion.height - 22,
            34f,
            14f,
            10,
            modeColor.color.rgb
        )
        Render2DUtils.drawImage(
            ResourceLocation("client/textures/ui/" + Konata.themeSlot + ".png"),
            (x + 43).toFloat(),
            y + Companion.height - 19,
            8f,
            8f,
            -1
        )
        Konata.fontManager.s16.drawString(
            Konata.i18n["theme.title"],
            (x + 20).toFloat(),
            y + Companion.height - 20,
            Konata.theme.categoryText.rgb
        )
        Konata.fontManager.s16.drawString(
            Konata.i18n["theme." + Konata.themeSlot],
            (x + 52).toFloat(),
            y + Companion.height - 20,
            Konata.theme.categoryTextSelected.rgb
        )
        if (Render2DUtils.isHovered((x + 40).toFloat(), y + Companion.height - 22, 34f, 14f, mouseX, mouseY)) {
            modeColor.base(Konata.theme.primary)
        } else {
            modeColor.base(Konata.theme.typeSelectionBackground)
        }

        // 绘制功能列表
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        Render2DUtils.doGlScissor(x.toFloat(), y.toFloat(), Companion.width, Companion.height - 4)
        moduleListAlpha = base(moduleListAlpha.toDouble(), 255.0, 0.1).toFloat()
        if (curType === Category.Music) {
            draw(x + leftWidth, y.toFloat(), Companion.width - leftWidth, Companion.height, mouseX, mouseY)
        } else if (curType === Category.ORNAMENTS) {
            ornamentPanel!!.drawScreen(
                x + leftWidth,
                y.toFloat(),
                Companion.width - leftWidth,
                Companion.height,
                mouseX,
                mouseY,
                partialTicks
            )
        } else {
            var modsY = y + 10f
            modHeight = 20f
            val containerWidth = Companion.width - leftWidth - 2
            modsContainer.draw(x + leftWidth, y + 10f, containerWidth, Companion.height - 20f, mouseX, mouseY) {
                for (m in mods) {
                    if (m.mod.category === curType) {
                        val moduleY = modsY + modsContainer.getScroll()
                        if (moduleY + 40 + m.height > y && moduleY < y + Companion.height) {
                            m.render(
                                x + leftWidth,
                                moduleY,
                                containerWidth,
                                40f,
                                mouseX.toFloat(),
                                mouseY.toFloat(),
                                curModule === m.mod
                            )
                        }
                        modsY += 40 + m.height
                        modHeight += 40 + m.height
                    }
                }
                modsContainer.setHeight(modHeight)
            }

        }
        GL11.glEnable(GL11.GL_BLEND)
        Render2DUtils.drawRect(
            x + leftWidth, y.toFloat(), Companion.width - leftWidth, Companion.height, Render2DUtils.reAlpha(
                Konata.theme.background, Render2DUtils.limit((255 - moduleListAlpha).toDouble())
            )
        )
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GL11.glPopMatrix()
    }

    override fun updateScreen() {
        super.updateScreen()
        val sr = ScaledResolution(mc)
        if (sr.scaledWidth < Companion.width) {
            x = 5
            Companion.width = (sr.scaledWidth - 10).toFloat()
        }
        if (sr.scaledHeight < Companion.height) {
            y = 5
            Companion.height = (sr.scaledHeight - 10).toFloat()
        }
    }

    override fun initGui() {
        super.initGui()
        val sr = ScaledResolution(mc)
        scaleAnimation.value = 0.6
        close = false
        if (Companion.width == 0f || Companion.height == 0f) {
            Companion.width = sr.scaledWidth / 2f
            Companion.height = sr.scaledHeight / 2f
        }
        if (x == -1 || y == -1) {
            x = ((sr.scaledWidth - Companion.width) / 2).toInt()
            y = ((sr.scaledHeight - Companion.height) / 2).toInt()
        }
        categories.clear()
        for (c in Category.entries) {
            categories.add(CategoryComponent(c))
        }
        if (mods.isEmpty()) {
            for (m in Konata.moduleManager.modules) {
                mods.add(ModuleRenderer(m))
            }
        }
        selection = (y + 70).toFloat()
        ornamentPanel = OrnamentPanel()
    }

    override fun onGuiClosed() {
        super.onGuiClosed()
        Konata.configManager.saveConfig("default")
    }

    @Throws(IOException::class)
    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == 1) {
            if (scaleAnimation.end != 0.1) {
                close = true
                scaleAnimation.fstart(scaleAnimation.value, 0.1, 0.2f, Type.EASE_IN_BACK)
            }
            return
        }
        for (m in mods) {
            if (m.mod.category === curType) {
                m.keyTyped(typedChar, keyCode)
            }
        }
        MusicPanel.keyTyped(typedChar, keyCode)
        super.keyTyped(typedChar, keyCode)
    }

    @Throws(IOException::class)
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        if (!Render2DUtils.isHovered(
                x.toFloat(),
                y.toFloat(),
                Companion.width,
                Companion.height,
                mouseX,
                mouseY
            )
        ) return
        // theme switch
        if (mouseButton == 0 && Render2DUtils.isHovered(
                (x + 40).toFloat(),
                y + Companion.height - 22,
                34f,
                14f,
                mouseX,
                mouseY
            )
        ) {
            if (Konata.themeSlot == "dark") {
                Konata.themeSlot = "light"
                Konata.theme = LightTheme()
            } else {
                Konata.themeSlot = "dark"
                Konata.theme = DarkTheme()
            }
        }

        if (mouseButton == 0 && Render2DUtils.isHovered(x.toFloat(), y.toFloat(), leftWidth, 34f, mouseX, mouseY)) {
            drag = true
            dragX = (mouseX - x).toFloat()
            dragY = (mouseY - y).toFloat()
        }
        if (mouseButton == 0 && Render2DUtils.isHovered(
                x + Companion.width - 20,
                y + Companion.height - 20,
                20f,
                20f,
                mouseX,
                mouseY
            ) && "null" == dragLock
        ) {
            val sr = ScaledResolution(mc)
            sizeDrag = true
            dragLock = "sizeDrag"
            sizeDragX = x + Companion.width - min(mouseX, sr.scaledWidth)
            sizeDragY = y + Companion.height - min(mouseY, sr.scaledHeight)
        }
        var my = (y + 60).toFloat()
        for (m in Category.entries) {
            if (Render2DUtils.isHovered(x.toFloat(), my - 8, leftWidth, 24f, mouseX, mouseY)) {
                wheelTemp = 0f
                modsWheel = 0f
                if (curType !== m) moduleListAlpha = 0f
                curType = m
            }
            my += 24f
        }
        if (curType === Category.Music) {
            MusicPanel.mouseClicked(mouseX, mouseY, mouseButton)
        } else if (curType === Category.ORNAMENTS) {
            ornamentPanel!!.mouseClicked(mouseX, mouseY, mouseButton)
        } else {
            // modules click
            var modsY = y + 10 + modsContainer.getRealScroll()
            for (m in mods) {
                if (m.mod.category === curType) {
                    m.mouseClick(
                        x + leftWidth,
                        modsY,
                        Companion.width - leftWidth,
                        40f,
                        mouseX.toFloat(),
                        mouseY.toFloat(),
                        mouseButton
                    )
                    modsY += 40 + m.height
                }
            }
        }
    }

    companion object {
        @JvmField
        var x = -1

        @JvmField
        var y = -1

        @JvmField
        var width = 0f

        @JvmField
        var height = 0f
        var bindLock = ""
        var curModule: Module? = null
        var dragLock = "null"
        var ornamentPanel: OrnamentPanel? = null
    }
}
