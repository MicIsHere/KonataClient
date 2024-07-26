package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.ModsList
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.utils.render.Render2DUtils
import cn.cutemic.konata.interfaces.ProviderManager
import java.awt.Color

class ModsListComponent : Component(ModsList::class.java) {
    init {
        this.x = 1f
    }
    var modules: List<Module> = ArrayList()
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val font = Konata.fontManager.s18
        var modY = 0f

        if ((mod as ModsList).showLogo.value) {
            Konata.fontManager.s36.drawString("FPS V3", x + 0.5f, y + 0.5f, Color(0, 0, 0, 150).rgb)
            Konata.fontManager.s36.drawString("FPS V3", x, y, Konata.theme.primary.rgb)
            modY = 20f
        }
        var width2 = 40f
        val x = x + this.width
        if (modules.isEmpty()) {
            modules =
                Konata.moduleManager.modules.sortedBy {
                    (if (mod.betterFont.value) font else ProviderManager.mcProvider.getFontRenderer()).getStringWidth(if ((mod as ModsList).english.value) it.name else Konata.i18n[it.name.lowercase()])
                }.reversed()
        }
        var ls = 0
        for (module in modules) {
            val col = Color.getHSBColor(
                ls / modules.size.toFloat() - ProviderManager.mcProvider.getPlayer()!!.ticksExisted % 50 / 50f,
                0.7f,
                1f
            )
            if (!module.isEnabled || module.category == Category.Interface)
                continue
            var name = Konata.i18n[module.name.lowercase()]
            if ((mod as ModsList).english.value)
                name = module.name
            var width = 0f
            width = if ((mod as ModsList).betterFont.value) {
                font.getStringWidth(name).toFloat()
            } else {
                ProviderManager.mcProvider.getFontRenderer().getStringWidth(name).toFloat()
            }

            if (width2 < width)
                width2 = width + 5

            Render2DUtils.drawRect(x - width - 4, y + modY, width + 4, 14f, (mod as ModsList).backgroundColor.color)
            var color = (mod as ModsList).color.color
            if ((mod as ModsList).rainbow.value) {
                color = col
            }
            if ((mod as ModsList).betterFont.value)
                font.drawStringWithShadow(name, x - width - 2, y + modY + 2, color.rgb)
            else
                ProviderManager.mcProvider.getFontRenderer()
                    .drawStringWithShadow(name, x - width - 2, y + modY, color.rgb)
            ls++
            modY += 14f
        }
        this.width = width2
        height = modY
    }
}