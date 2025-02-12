package cn.cutemic.konata.modules.music

import cn.cutemic.konata.Konata
import java.io.File
import kotlin.math.min

object MusicPlayer {
    var playList = PlayList()
    var mode = 0
    var startTime: Long = 0
    var isPlaying = false
    private var volume = 1f
    private var curPlayProgress = 0f
    val playProgress: Float
        get() {
            if (isPlaying) if (JLayerHelper.clip != null) curPlayProgress = JLayerHelper.progress
            return min(curPlayProgress, 1f)
        }

    fun play() {
        isPlaying = true
        if (JLayerHelper.clip == null) return
        JLayerHelper.start()
    }

    fun pause() {
        isPlaying = false
        if (JLayerHelper.clip == null) return
        JLayerHelper.stop()
    }

    fun stop() {
        isPlaying = false
        if (JLayerHelper.clip == null) return
        JLayerHelper.stop()
    }

    @JvmStatic
    fun playFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            JLayerHelper.convert(path, path.replace(".mp3", ".wav"))
            if (JLayerHelper.clip != null) {
                JLayerHelper.clip!!.stop()
                JLayerHelper.clip!!.close()
            }
            val v = Konata.configManager.configure.getOrCreate("volume", "1").toFloat()
            Konata.async.runnable {
                JLayerHelper.playWAV(path.replace(".mp3", ".wav"))
                setVolume(v)
            }
        }
    }

    fun getVolume(): Float {
        return volume
    }

    fun setVolume(volume: Float) {
        MusicPlayer.volume = volume
        if (JLayerHelper.clip == null) return
        JLayerHelper.setVolume(volume)
        Konata.configManager.configure["volume"] = volume.toString()
    }
}
