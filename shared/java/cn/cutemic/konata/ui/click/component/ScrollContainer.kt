package cn.cutemic.konata.ui.click.component

import org.lwjgl.input.Mouse
import cn.cutemic.konata.ui.click.MainPanel.Companion.dragLock
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color

class ScrollContainer {
    private var wheel = 0f
    private var wheel_anim = 0f
    private var height = 0f

    private var scrollExpand = 0.0
    private var scrollStart = 0f
    private var isScrolling = false

    fun draw(x: Float, y: Float, width: Float, height: Float, mouseX: Int, mouseY: Int, runnable: Runnable) {
        runnable.run()

        // if the scroll bar needs to be render
        if (this.height > height) {
            // calc scroll bar height
            val percent = (height / this.height)
            val sHeight = percent * height
            val scrollPercent = (getScroll() / (this.height - height))
            val sY = y - scrollPercent * (height - sHeight)
            val sX = x + width + 1 - scrollExpand.toFloat()
            Render2DUtils.drawRect(
                sX,
                sY,
                1f + scrollExpand.toFloat(),
                sHeight,
                Color(
                    255, 255, 255, 200
                )
            )
            if (Render2DUtils.isHovered(
                    sX - 1,
                    sY,
                    2f + scrollExpand.toFloat(),
                    sHeight, mouseX, mouseY
                )
            ) {
                scrollExpand = 1.0
                if (Mouse.isButtonDown(0)) {
                    if (!isScrolling && dragLock == "null") {
                        isScrolling = true
                        dragLock = this.javaClass.simpleName
                        scrollStart = mouseY - sY
                    }
                }
            } else if (!isScrolling) {
                scrollExpand = 0.0
            }

            if (isScrolling) {
                if (Mouse.isButtonDown(0)) {
                    wheel_anim = -((mouseY - scrollStart - y) / height) * this.height
                } else {
                    isScrolling = false
                    dragLock = "null"
                }
            }
        } else {
            wheel_anim = 0f
        }

        if (this.height > height) {
            //mods列表滑动
            val mouseDWheel = Mouse.getDWheel()
            if (mouseDWheel > 0) {
                wheel_anim += 20f
            } else if (mouseDWheel < 0) {
                wheel_anim -= 20f
            }
            val maxUp = this.height - height
            wheel_anim = wheel_anim.coerceIn(-maxUp, 0f)
        }
        wheel = base(wheel.toDouble(), wheel_anim.toDouble(), 0.2).toFloat()
    }

    fun setHeight(height: Float) {
        this.height = height
    }

    fun getHeight(): Float {
        return this.height
    }

    fun getScroll(): Float {
        return wheel
    }

    fun getRealScroll(): Float {
        return wheel_anim
    }
}