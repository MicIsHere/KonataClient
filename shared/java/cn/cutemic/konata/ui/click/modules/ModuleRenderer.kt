package cn.cutemic.konata.ui.click.modules

import net.minecraft.util.ResourceLocation
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.Setting
import cn.cutemic.konata.features.settings.impl.*
import cn.cutemic.konata.ui.click.MainPanel
import cn.cutemic.konata.ui.click.modules.impl.*
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.math.animation.Type
import cn.cutemic.konata.utils.render.Render2DUtils
import java.util.*
import java.util.function.Consumer

class ModuleRenderer(override var mod: Module) : ValueRender() {
    private var settingsRenderers = ArrayList<SettingRender<*>>()
    private var settingHeight = 0f
    private var border = 0f
    private var expand = false
    private var background = ColorAnimation()
    var content = ColorAnimation(Konata.theme.moduleDisabled)

    init {
        content =
            ColorAnimation(if (mod.isEnabled) Konata.theme.moduleEnabled else Konata.theme.moduleDisabled)
        mod.settings.forEach(Consumer { setting: Setting<*>? ->
            when (setting) {
                is BooleanSetting -> {
                    settingsRenderers.add(BooleanSettingRender(mod, (setting as BooleanSetting?)!!))
                }

                is ModeSetting -> {
                    settingsRenderers.add(ModeSettingRender(mod, (setting as ModeSetting?)!!))
                }

                is TextSetting -> {
                    settingsRenderers.add(TextSettingRender(mod, (setting as TextSetting?)!!))
                }

                is NumberSetting -> {
                    settingsRenderers.add(NumberSettingRender(mod, (setting as NumberSetting?)!!))
                }

                is ColorSetting -> {
                    settingsRenderers.add(ColorSettingRender(mod, (setting as ColorSetting?)!!))
                }

                is BindSetting -> {
                    settingsRenderers.add(BindSettingRender(mod, (setting as BindSetting?)!!))
                }
            }
        })
    }

    override fun render(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        mouseX: Float,
        mouseY: Float,
        current: Boolean
    ) {
        content.update()
        background.update()
        border = if (Render2DUtils.isHovered(x + 5, y, width - 10, height, mouseX.toInt(), mouseY.toInt())) {
            base(border.toDouble(), 200.0, 0.3).toFloat()
        } else {
            base(border.toDouble(), 30.0, 0.3).toFloat()
        }
        if (mod.isEnabled) {
            content.start(content.color, Konata.theme.moduleTextEnabled, 0.2f, Type.EASE_IN_OUT_QUAD)
            background.start(background.color, Konata.theme.moduleEnabled, 0.2f, Type.EASE_IN_OUT_QUAD)
        } else {
            content.start(content.color, Konata.theme.moduleTextDisabled, 0.2f, Type.EASE_IN_OUT_QUAD)
            background.start(background.color, Konata.theme.moduleDisabled, 0.2f, Type.EASE_IN_OUT_QUAD)
        }
        Render2DUtils.drawOptimizedRoundedRect(
            x + 4.5f,
            y - 0.5f,
            width - 9,
            settingHeight + 38f,
            Konata.theme.moduleEnabled
        )
        Render2DUtils.drawOptimizedRoundedRect(
            x + 5,
            y,
            width - 10,
            settingHeight + 37f,
            Konata.theme.moduleDisabled.rgb
        )
        Render2DUtils.drawOptimizedRoundedBorderRect(
            x + 5, y, width - 10, 37f, 0.5f, background.color, Render2DUtils.reAlpha(
                Konata.theme.moduleBorder, border.toInt()
            )
        )
        if (mod.category === Category.Interface) {
            Render2DUtils.drawImage(
                ResourceLocation("client/textures/modules/interface.png"),
                x + 14,
                y + 10,
                14f,
                14f,
                content.color.rgb
            )
        } else {
            Render2DUtils.drawImage(
                ResourceLocation("client/textures/modules/" + mod.name.lowercase(Locale.getDefault()) + ".png"),
                x + 14,
                y + 10,
                14f,
                14f,
                content.color.rgb
            )
        }
        Konata.fontManager.s18.drawString(
            Konata.i18n[mod.name.lowercase(Locale.getDefault())],
            x + 40,
            y + 9,
            content.color.rgb
        )
        Konata.fontManager.s16.drawString(
            Konata.i18n[mod.name.lowercase(Locale.getDefault()) + ".desc"],
            x + 40,
            y + 20,
            Konata.theme.textColorDescription.rgb
        )
        var settingsHeight = 0f
        if (expand) {
            for (settingsRenderer in settingsRenderers) {
                if (settingsRenderer.setting.getVisible()) {
                    settingsRenderer.render(
                        x + 5,
                        y + 40 + settingsHeight,
                        width - 10,
                        12f,
                        mouseX,
                        mouseY,
                        MainPanel.curModule === mod
                    )
                    settingsHeight += settingsRenderer.height + 6
                }
            }
        }
        settingHeight = base(settingHeight.toDouble(), settingsHeight.toDouble(), 0.2).toFloat()
        this.height = settingHeight
    }

    override fun mouseClick(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, btn: Int) {
        var settingsHeight = 0f
        if (expand) {
            for (settingsRenderer in settingsRenderers) {
                if (settingsRenderer.setting.getVisible()) {
                    settingsRenderer.mouseClick(x + 5, y + 40 + settingsHeight, width - 10, 12f, mouseX, mouseY, btn)
                    settingsHeight += settingsRenderer.height + 6
                }
            }
        }
        if (Render2DUtils.isHovered(x + 5, y, width - 10, 40f, mouseX.toInt(), mouseY.toInt())) {
            if (btn == 0) {
                mod.toggle()
            } else if (btn == 1) {
                expand = !expand
                MainPanel.curModule = null
            }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (expand) {
            for (settingsRenderer in settingsRenderers) {
                settingsRenderer.keyTyped(typedChar, keyCode)
            }
        }
    }
}
