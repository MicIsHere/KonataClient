package cn.cutemic.konata.ui.custom.impl

import net.minecraft.util.ResourceLocation
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.interfaces.MusicOverlay
import cn.cutemic.konata.modules.music.MusicPlayer
import cn.cutemic.konata.modules.music.MusicPlayer.playProgress
import cn.cutemic.konata.modules.music.netease.Music
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.ui.custom.Position
import cn.cutemic.konata.utils.math.animation.AnimationUtils.base
import cn.cutemic.konata.utils.render.Render2DUtils
import kotlin.math.max

class MusicComponent : Component(MusicOverlay::class.java) {
    init {
        x = 0.01f
        y = 0.01f
        position = Position.RT
    }

    private fun drawSong(x: Float, y: Float, width: Float, height: Float) {
        val current = MusicPlayer.playList.getCurrent() as Music?
        drawRect(x, y, width, height, mod.backgroundColor.color)
        drawRect(x, y, songProgress, height, MusicOverlay.progressColor.color)
        songProgress = base(songProgress.toDouble(), (6 + (width - 6) * playProgress).toDouble(), 0.1).toFloat()
        Render2DUtils.drawImage(
            ResourceLocation("music/netease/" + current!!.id),
            x + 5,
            y + 5,
            height - 10,
            height - 10,
            -1
        )
        val s18 = Konata.fontManager.s18
        val s16 = Konata.fontManager.s16
        drawString(s18, current.name, x + 40, y + 6, Konata.theme.textColorTitle.rgb)
        drawString(s16, current.author, x + 40, y + 18, Konata.theme.textColorDescription.rgb)
    }

    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val s18 = Konata.fontManager.s18
        if (!MusicPlayer.playList.musics.isEmpty()) {
            val width: Float = max(
                getStringWidth(s18, MusicPlayer.playList.getCurrent()!!.name), s18.getStringWidth(
                    MusicPlayer.playList.getCurrent()!!.author!!
                ).toFloat()
            )
            drawSong(x, y, width + 60, 40f)
            this.width = width + 60
            height = 40f
        }
    }

    companion object {
        private var songProgress = 0f
    }
}
