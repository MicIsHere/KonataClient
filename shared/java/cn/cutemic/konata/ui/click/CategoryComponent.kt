package cn.cutemic.konata.ui.click

import net.minecraft.util.ResourceLocation
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.utils.math.animation.ColorAnimation
import cn.cutemic.konata.utils.math.animation.Type
import cn.cutemic.konata.utils.render.Render2DUtils
import java.util.*

class CategoryComponent(@JvmField var category: Category) {
    private var animationName = ColorAnimation()
    @JvmField
    var categorySelectionColor = ColorAnimation()

    init {
        animationName.color = Konata.theme.categoryText
    }

    fun render(x: Float, y: Float, width: Float, height: Float, mouseX: Float, mouseY: Float, selected: Boolean) {
        animationName.start(
            animationName.color,
            if (selected) Konata.theme.categoryTextSelected else Konata.theme.categoryText,
            0.2f,
            Type.EASE_IN_OUT_QUAD
        )
        animationName.update()
        Render2DUtils.drawImage(
            ResourceLocation("client/gui/settings/icons/" + category.category.lowercase() + ".png"),
            x + 12,
            y - 2,
            12f,
            12f,
            animationName.color
        )
        Konata.fontManager.s16.drawString(
            Konata.i18n["category." + category.category.lowercase(
                Locale.getDefault()
            )], x + 30, y, animationName.color.rgb
        )
    }
}
