package cn.cutemic.konata.modules.music

import cn.cutemic.konata.Konata
import cn.cutemic.konata.ui.notification.NotificationManager
import java.util.*

class PlayList {
    var musics = LinkedList<AbstractMusic>()
    var current = 0
    var shuffled = false

    fun add(music: AbstractMusic) {
        musics.add(music)
    }


    fun play() {
        NotificationManager.addNotification(
            Konata.i18n["notification.music"],
            Konata.i18n["notification.music.next"].replace(
                "%s",
                MusicPlayer.playList.getCurrent()?.name ?: ""
            ),
            2f
        )
        musics[current].play()
        MusicPlayer.isPlaying = true
    }

    fun getCurrent(): AbstractMusic? {
        return if (musics.isEmpty() || current < 0) null else musics[current]
    }

    fun pause() {
        MusicPlayer.pause()
    }

    fun seek(percent: Float) {
        musics[current].seek(percent)
    }

    private fun shuffleList() {
        if (MusicPlayer.mode == 0 && !shuffled) {
            val current1 = MusicPlayer.playList.getCurrent()
            MusicPlayer.playList.musics.shuffle()
            MusicPlayer.playList.current = MusicPlayer.playList.musics.indexOf(current1)
            shuffled = true
        }
    }

    operator fun next() {
        MusicPlayer.stop()
        if (musics.size == 0)
            return
        shuffleList()
        if (MusicPlayer.mode != 2) {
            current++
            if (current >= musics.size) {
                current = 0
            }
        }
        NotificationManager.addNotification(
            Konata.i18n["notification.music"],
            Konata.i18n["notification.music.next"].replace(
                "%s",
                MusicPlayer.playList.getCurrent()?.name ?: ""
            ),
            2f
        )
        musics[current].play()
        MusicPlayer.isPlaying = true
    }

    fun previous() {
        MusicPlayer.stop()
        if (musics.size == 0)
            return
        shuffleList()
        if (MusicPlayer.mode != 2) {
            current--
            if (current < 0) {
                current = musics.size - 1
            }
        }
        NotificationManager.addNotification(
            Konata.i18n["notification.music"],
            Konata.i18n["notification.music.next"].replace(
                "%s",
                MusicPlayer.playList.getCurrent()?.name ?: ""
            ),
            2f
        )
        musics[current].play()
    }

    fun remove(music: AbstractMusic) {
        musics.remove(music)
    }

    fun remove(index: Int) {
        musics.removeAt(index)
    }

    fun clear() {
        musics.clear()
    }

    fun setMusicList(musics: LinkedList<AbstractMusic>) {
        val element = getCurrent()
        this.musics.clear()
        this.musics.addAll(musics)
        if (element != null)
            current = musics.indexOf(element)
        shuffled = false
    }
}
