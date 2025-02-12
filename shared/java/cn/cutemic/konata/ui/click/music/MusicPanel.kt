package cn.cutemic.konata.ui.click.music

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import cn.cutemic.konata.Konata
import cn.cutemic.konata.modules.music.JLayerHelper
import cn.cutemic.konata.modules.music.MusicPlayer
import cn.cutemic.konata.modules.music.PlayList
import cn.cutemic.konata.modules.music.netease.Music
import cn.cutemic.konata.modules.music.netease.NeteaseApi
import cn.cutemic.konata.modules.music.netease.deserialize.MusicWrapper
import cn.cutemic.konata.ui.click.component.ScrollContainer
import cn.cutemic.konata.utils.os.FileUtils
import cn.cutemic.konata.utils.os.FileUtils.saveFileBytes
import cn.cutemic.konata.utils.os.FileUtils.saveTempValue
import cn.cutemic.konata.utils.render.Render2DUtils
import java.awt.Color
import java.io.File
import java.util.*

object MusicPanel {
    private var displayList: PlayList = PlayList()
    private var recommendList: PlayList = PlayList()
    private var searchList = PlayList()
    private var searchThread: Thread? = null

    private var playProgress = 0f
    private var inputBox = SearchBox(
        Konata.i18n["music.search"]
    ) {
        searchThread = Thread { run() }
        searchThread!!.start()
    }
    private var pages = arrayOf("music.name", "music.list", "music.daily")
    private var curSearch = 0
    private var isWaitingLogin = false
    private var key: String? = null
    private var loginThread: Thread? = null
    private val container = ScrollContainer()

    var code = 801
    var nickname: String = "Unknown"
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    @JvmStatic
    fun mouseClicked(mouseX: Int, mouseY: Int, btn: Int) {
        inputBox.mouseClicked(mouseX, mouseY, btn)
        if (searchThread == null || !searchThread!!.isAlive) {
            var dY = y + 50 + container.getRealScroll()
            for (music in displayList.musics) {
                if (Render2DUtils.isHovered(
                        x,
                        dY,
                        width - 10f,
                        40f,
                        mouseX,
                        mouseY
                    ) && mouseY < y + height - 34 && mouseY > y + 34
                ) {
                    if (Mouse.isButtonDown(0)) {
                        music.play()
                        MusicPlayer.isPlaying = true
                        MusicPlayer.playList.current = MusicPlayer.playList.musics.indexOf(music)
                    }
                }
                dY += 40f
            }
        }


        //搜索
        var xOffset = 0
        for ((i, page) in pages.withIndex()) {
            val width = Konata.fontManager.s16.getStringWidth(Konata.i18n[page]) + 10
            if (Render2DUtils.isHovered(x + 95 + xOffset, y + 8, width.toFloat(), 14f, mouseX, mouseY)) {
                if (Mouse.isButtonDown(0)) {
                    curSearch = i
                    if (curSearch == 2) {
                        recommendList = MusicWrapper.songsFromDaily
                        displayList = recommendList
                        MusicPlayer.playList.pause()
                        setMusicList()
                    }
                }
            }
            xOffset += width
        }


        // 操作栏
        val current = MusicPlayer.playList.getCurrent()
        if (Render2DUtils.isHovered(x, y + height - 30, width, 4f, mouseX, mouseY)) {
            if (Mouse.isButtonDown(0) && current != null) {
                if (!MusicPlayer.isPlaying) {
                    Konata.async.runnable {
                        MusicPlayer.playList.play()
                        try {
                            Thread.sleep(50)
                        } catch (e: InterruptedException) {
                            throw RuntimeException(e)
                        }
                        current.seek((mouseX - x) / width)
                    }
                    MusicPlayer.isPlaying = true
                } else {
                    current.seek((mouseX - x) / width)
                }
            }
        }
        if (Render2DUtils.isHovered(x + width / 2 - 35, y + height - 23, 16f, 16f, mouseX, mouseY) && btn == 0) {
            MusicPlayer.playList.previous()

        }
        if (Render2DUtils.isHovered(x + width / 2 + 5, y + height - 23, 16f, 16f, mouseX, mouseY) && btn == 0) {
            MusicPlayer.playList.next()
        }
        if (Render2DUtils.isHovered(
                x + width / 2 - 15,
                y + height - 23,
                35 / 2f,
                35 / 2f,
                mouseX,
                mouseY
            ) && btn == 0
        ) {
            if (!MusicPlayer.playList.musics.isEmpty()) {
                if (MusicPlayer.isPlaying) {
                    playProgress = MusicPlayer.playProgress
                    MusicPlayer.playList.pause()
                    MusicPlayer.isPlaying = false
                } else {
                    Konata.async.runnable {
                        MusicPlayer.playList.getCurrent()?.play()
                        try {
                            Thread.sleep(50)
                        } catch (e: InterruptedException) {
                            throw RuntimeException(e)
                        }
                        MusicPlayer.playList.getCurrent()?.seek(playProgress)
                    }
                    MusicPlayer.isPlaying = true
                }
            }
        }
        if (Render2DUtils.isHovered(x + width / 2 - 55, y + height - 21, 12f, 12f, mouseX, mouseY) && btn == 0) {
            if (MusicPlayer.mode < 2) {
                MusicPlayer.mode++
                MusicPlayer.playList.setMusicList(displayList.musics)
            } else {
                MusicPlayer.mode = 0
            }
        }
    }

    @JvmStatic
    fun keyTyped(c: Char, keyCode: Int) {
        inputBox.keyTyped(c, keyCode)
    }

    @JvmStatic
    fun draw(x: Float, y: Float, width: Float, height: Float, mouseX: Int, mouseY: Int) {
        if (isWaitingLogin) {
            Konata.fontManager.s18.drawCenteredString(
                "<",
                x + 20,
                y + 20,
                Konata.theme.textColorTitle.rgb
            )
            if (Render2DUtils.isHovered(
                    x + 20,
                    y + 20,
                    20f,
                    20f,
                    mouseX,
                    mouseY
                ) && Mouse.isButtonDown(0)
            ) isWaitingLogin = false
            try {
                val resourceLocation = ResourceLocation("music/qr")
                Render2DUtils.drawImage(resourceLocation, x + width / 2 - 45, y + height / 2 - 45, 90f, 90f, -1)
            } catch (e: Exception) {

            }
            var scan = ""
            when (code) {
                801 -> scan = "music.waitscan"
                802 -> scan =
                    "music.waitconfirmation"

                800 -> {
                    reloadImg()
                    code = 801
                }

                803 -> {
                    isWaitingLogin = false
                }
            }
            Konata.fontManager.s18.drawCenteredString(
                Konata.i18n[scan],
                x + width / 2,
                y + height / 2 + 60,
                Konata.theme.textColorTitle.rgb
            )
            return
        }
        MusicPanel.x = x
        MusicPanel.y = y
        MusicPanel.width = width
        MusicPanel.height = height
        if (displayList.musics.isEmpty() && searchThread == null) {
            searchThread = Thread { searchList = MusicWrapper.searchSongs("Minecraft") }
            searchThread!!.start()
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        Render2DUtils.doGlScissor(x, y + 30, width, height - 60)
        if (searchThread == null || !searchThread!!.isAlive) {
            var dY = y + 50 + container.getScroll()
            var musicHeight = 0f

            Render2DUtils.drawRect(x, dY - 6, width - 10, 0.5f, Color(100, 100, 100, 50))
            Konata.fontManager.s16.drawString(
                "#",
                x + 12,
                dY - 20,
                Konata.theme.textColorTitle.rgb
            )
            Konata.fontManager.s16.drawString(
                "标题",
                x + 30,
                dY - 20,
                Konata.theme.textColorTitle.rgb
            )

            // music list
            container.draw(x, y + 50, width - 5, height - 80, mouseX, mouseY) {
                for ((i, music) in displayList.musics.withIndex()) {
                    if (Render2DUtils.isHovered(
                            x,
                            dY,
                            width - 10f,
                            40f,
                            mouseX,
                            mouseY
                        ) && mouseY > y + 50 && mouseY < y + height - 34
                    ) {
                        Render2DUtils.drawOptimizedRoundedRect(x, dY, width - 10, 40f, Color(200, 200, 200, 50))
                    }
                    Konata.fontManager.s16.drawCenteredString(
                        "" + i,
                        x + 15,
                        dY + 15,
                        Konata.theme.textColorTitle.rgb
                    )
                    if (dY > y && dY < y + height - 10) if ((music as Music).isLoadedImage) {
                        Render2DUtils.drawImage(
                            ResourceLocation("music/netease/" + music.id),
                            x + 30,
                            dY + 10,
                            20f,
                            20f,
                            -1
                        )
                    } else {
                        Render2DUtils.drawOptimizedRoundedRect(
                            x + 30,
                            dY + 10,
                            20f,
                            20f,
                            Konata.theme.musicBlank
                        )
                    }
                    if (MusicPlayer.playList.current == i) {
                        Konata.fontManager.s16.drawString(
                            music.name + "  " + music.author,
                            x + 60,
                            dY + 10,
                            Konata.theme.textColorTitle.rgb
                        )
                        Konata.fontManager.s16.drawString(
                            music.author,
                            x + 60,
                            dY + 20,
                            Konata.theme.textColorDescription.rgb
                        )
                    } else {
                        Konata.fontManager.s16.drawString(
                            music.name + "  " + music.author,
                            x + 60,
                            dY + 10,
                            Konata.theme.textColorTitle.rgb
                        )
                        Konata.fontManager.s16.drawString(
                            music.author,
                            x + 60,
                            dY + 20,
                            Konata.theme.textColorDescription.rgb
                        )
                    }
                    dY += 40f
                    musicHeight += 40f
                }
                container.setHeight(musicHeight)
            }

        } else {
            Konata.fontManager.s18.drawCenteredString(
                "...",
                x + width / 2,
                y + 60,
                Konata.theme.textColorTitle.rgb
            )
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        // 搜索
        inputBox.render(x + 5, y + 6, 80f, 16f, mouseX, mouseY)

        // 分页
        var xOffset = 0
        var pagesWidth = 0
        for (page in pages) {
            pagesWidth += Konata.fontManager.s16.getStringWidth(Konata.i18n[page]) + 10
        }
        Render2DUtils.drawOptimizedRoundedRect(
            x + 90,
            y + 6,
            pagesWidth.toFloat(),
            16f,
            Konata.theme.frontBackground
        )
        for (page in pages) {
            val stringWidth = Konata.fontManager.s16.getStringWidth(Konata.i18n[page])
            if (page == pages[curSearch]) {
                Render2DUtils.drawOptimizedRoundedRect(
                    x + 90 + xOffset,
                    y + 6,
                    (stringWidth + 10).toFloat(),
                    16f,
                    Konata.theme.primary
                )
                Konata.fontManager.s16.drawString(
                    Konata.i18n[page],
                    x + 95 + xOffset,
                    y + 10,
                    Konata.theme.textColorTitle.rgb
                )
            } else {
                Konata.fontManager.s16.drawString(
                    Konata.i18n[page],
                    x + 95 + xOffset,
                    y + 10,
                    Konata.theme.textColorDescription.rgb
                )
            }
            xOffset += stringWidth + 10
        }

        // login
        if (NeteaseApi.cookies.isEmpty()) {
            if (nickname.isNotEmpty()) nickname = "Unknown"
            val stringWidth =
                Konata.fontManager!!.s16.getStringWidth(Konata.i18n["music.notLoggedIn"])
            if (Render2DUtils.isHovered(
                    x + width - stringWidth - 5,
                    y + 10,
                    stringWidth.toFloat(),
                    16f,
                    mouseX,
                    mouseY
                )
            ) {
                Konata.fontManager!!.s16.drawString(
                    Konata.i18n["music.notloggedin"],
                    x + width - stringWidth - 5,
                    y + 10,
                    Konata.theme.textColorTitle.rgb
                )
                if (Mouse.isButtonDown(0)) {
                    isWaitingLogin = true
                    reloadImg()
                    code = 801
                }
            } else {
                Konata.fontManager.s16.drawString(
                    Konata.i18n["music.notloggedin"],
                    x + width - stringWidth - 5,
                    y + 10,
                    Konata.theme.textColorDescription.rgb
                )
            }
        } else {
            val stringWidth = Konata.fontManager.s16.getStringWidth(nickname)
            Konata.fontManager.s16.drawString(
                nickname,
                x + width - stringWidth - 5,
                y + 10,
                Konata.theme.textColorTitle.rgb
            )
            if (Render2DUtils.isHovered(
                    x + width - stringWidth - 5,
                    y + 10,
                    stringWidth.toFloat(),
                    16f,
                    mouseX,
                    mouseY
                )
            ) {
                if (Mouse.isButtonDown(0)) {
                    isWaitingLogin = true
                    reloadImg()
                    code = 801
                }
            }
        }

        // 操作栏
        val current = MusicPlayer.playList.getCurrent()
        Render2DUtils.drawRect(x, y + height - 30, width, 2f, Konata.theme.frontBackground.rgb)
        Render2DUtils.drawRect(
            x,
            y + height - 30,
            width * MusicPlayer.playProgress,
            2f,
            Konata.theme.primary.rgb
        )
        if (Render2DUtils.isHovered(x, y + height - 32, width, 4f, mouseX, mouseY)) {
            Render2DUtils.drawRect(
                x,
                y + height - 31f,
                width * MusicPlayer.playProgress,
                4f,
                Konata.theme.primary.rgb
            )
        }

        // 音量
        Render2DUtils.drawImage(
            ResourceLocation("client/textures/ui/volume.png"),
            x + width - 50,
            y + height - 16,
            7f,
            7f,
            Konata.theme.textColorTitle
        )
        Render2DUtils.drawRect(x + width - 40, y + height - 14, 30f, 2f, Konata.theme.frontBackground.rgb)
        Render2DUtils.drawRect(
            x + width - 40,
            y + height - 14,
            30 * MusicPlayer.getVolume(),
            2f,
            Konata.theme.primary.rgb
        )
        if (Render2DUtils.isHovered(x + width - 40, y + height - 14, 30f, 2f, mouseX, mouseY)) {
            Render2DUtils.drawRect(
                x + width - 40,
                y + height - 14.5f,
                30 * MusicPlayer.getVolume(),
                3f,
                Konata.theme.primary.rgb
            )
            if (Mouse.isButtonDown(0)) {
                val newVolume = (mouseX - x - width + 40) / 30f
                MusicPlayer.setVolume(newVolume)
            }
        }
        val trimWidth = (width / 2 - 100).toInt()
        if (!MusicPlayer.playList.musics.isEmpty() && current != null) {
            val name = Konata.fontManager.s18.trimString(
                current.name + " - " + current.author,
                trimWidth.toFloat(),
                false
            )
            val s16b = Konata.fontManager.s16
            Konata.fontManager.s18.drawString(
                name,
                x + 30,
                y + height - 23,
                Konata.theme.textColorTitle.rgb
            )
            var progress = "0:00/0:00"
            if (JLayerHelper.clip != null) {
                val duration = JLayerHelper.duration
                val minutes = (duration * MusicPlayer.playProgress).toInt()
                val seconds = ((duration * MusicPlayer.playProgress - minutes) * 60).toInt()
                progress =
                    minutes.toString() + ":" + seconds + "/" + duration.toInt() + ":" + ((duration - duration.toInt()) * 60).toInt()
            }
            s16b.drawString(progress, x + 30, y + height - 14, Konata.theme.textColorDescription.rgb)
            if (MusicPlayer.playList.getCurrent() !=null && (MusicPlayer.playList.getCurrent() as Music).isLoadedImage) Render2DUtils.drawImage(
                ResourceLocation("music/netease/" + (MusicPlayer.playList.getCurrent() as Music).id),
                x + 5,
                y + height - 24,
                20f,
                20f,
                -1
            ) else Render2DUtils.drawOptimizedRoundedRect(
                x + 5,
                y + height - 24,
                20f,
                20f,
                Konata.theme.musicBlank
            )
        }
        var res = ResourceLocation("client/gui/settings/music/loop.png")
        when (MusicPlayer.mode) {
            0 -> {
                res = ResourceLocation("client/gui/settings/music/shuffle.png")
            }

            1 -> {
                res = ResourceLocation("client/gui/settings/music/order.png")
            }

            2 -> {
                res = ResourceLocation("client/gui/settings/music/loop.png")
            }
        }
        Render2DUtils.drawImage(res, x + width / 2 - 55, y + height - 21, 12f, 12f, Konata.theme.textColorTitle)
        Render2DUtils.drawImage(
            ResourceLocation("client/gui/settings/music/previous.png"),
            x + width / 2 - 35,
            y + height - 23,
            16f,
            16f,
            Konata.theme.textColorTitle
        )
        Render2DUtils.drawImage(
            if (MusicPlayer.isPlaying) ResourceLocation("client/gui/settings/music/pause.png") else ResourceLocation(
                "client/gui/settings/music/play.png"
            ), x + width / 2 - 15, y + height - 23, 35 / 2f, 35 / 2f, -1
        )
        Render2DUtils.drawImage(
            ResourceLocation("client/gui/settings/music/next.png"),
            x + width / 2 + 5,
            y + height - 23,
            16f,
            16f,
            Konata.theme.textColorTitle
        )
    }

    private fun extractMiddleContent(input: String, prefix: String, suffix: String): String {
        val startIndex = input.indexOf(prefix)
        if (startIndex != -1) {
            val endIndex = input.indexOf(suffix, startIndex + prefix.length)
            if (endIndex != -1) {
                return input.substring(startIndex + prefix.length, endIndex)
            }
        }
        return ""
    }

    private fun reloadImg() {
        loginThread = Thread {
            while (isWaitingLogin) {
                try {
                    val loginStatus = MusicWrapper.getLoginStatus(key)
                    code = loginStatus["code"].asInt
                    if (code == 802) {
                        val element = loginStatus["nickname"]
                        if (element != null) {
                            nickname = element.asString
                            saveTempValue("nickname", nickname)
                        }
                    }
                    if (code == 803) {
                        // parse cookie
                        var asString = loginStatus["cookie"].asString
                        val result = "MUSIC_U=" + extractMiddleContent(
                            asString,
                            "MUSIC_U=",
                            ";"
                        ) + "; " + "NMTID=" + extractMiddleContent(asString, "NMTID=", ";")
                        NeteaseApi.cookies = result

                        saveTempValue("cookies", NeteaseApi.cookies)
                        println("cookies: " + NeteaseApi.cookies)
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
        }
        Konata.async.runnable(Runnable {
            key = MusicWrapper.qRKey
            if (key == null) return@Runnable
            val base64 = MusicWrapper.getQRCodeImg(key)
            // render base64 img data
            val textureManager = Minecraft.getMinecraft().textureManager
            // base64 decode
            val bytes = Base64.getDecoder().decode(base64)
            // create resource location
            val resourceLocation = ResourceLocation("music/qr")
            val qr = File(FileUtils.dir, "/music/qr.png")
            val qrf = File(FileUtils.dir, "/music")
            qrf.mkdirs()
            saveFileBytes("/music/qr.png", bytes)
            val textureArt = ThreadDownloadImageData(qr, null, null, null)
            textureManager.loadTexture(resourceLocation, textureArt)
            loginThread!!.start()
        })
    }

    private fun setMusicList() {
        MusicPlayer.playList.setMusicList(displayList.musics)
    }

    private fun run() {
        if (inputBox.content.isNotEmpty()) {
            searchList = if (curSearch == 0) {
                MusicWrapper.searchSongs(inputBox.content)
            } else {
                MusicWrapper.searchList(inputBox.content)
            }
            displayList = searchList
            MusicPlayer.playList.pause()
            setMusicList()
            searchThread = null
        }
    }
}
