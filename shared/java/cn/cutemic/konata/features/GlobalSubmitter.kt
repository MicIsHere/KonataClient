package cn.cutemic.konata.features

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.EventDispatcher.registerListener
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventPacket
import cn.cutemic.konata.event.events.EventRender2D
import cn.cutemic.konata.event.events.EventSendChatMessage
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.features.impl.utility.Translator
import cn.cutemic.konata.interfaces.ProviderManager
import cn.cutemic.konata.modules.music.MusicPlayer
import cn.cutemic.konata.ui.notification.NotificationManager
import cn.cutemic.konata.ui.screens.mainmenu.MainMenu
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.math.MathTimer
import cn.cutemic.konata.utils.thirdparty.openai.OpenAi
import cn.cutemic.konata.utils.thirdparty.openai.requestClientAI
import cn.cutemic.konata.utils.render.shader.BlurBuffer
import cn.cutemic.konata.wrapper.TextFormattingProvider

class GlobalSubmitter {


    var time = MathTimer()
    private var musicSwitchTimer = MathTimer()

    fun init() {
        registerListener(this)
    }

    @Subscribe
    fun onChat(e: EventPacket) {
        if (Translator.using) {
            if (ProviderManager.packetChat.isPacket(e.packet)) {
                ProviderManager.packetChat.appendTranslation(e.packet)
            }
        }
    }

    @Subscribe
    fun onChatSend(e: EventSendChatMessage) {
        val msg = e.msg
        if (Translator.using) {
            if (msg.startsWith("#TRANSLATE")) {
                val message = msg.replace("#TRANSLATE", "")
                NotificationManager.addNotification(Konata.i18n["translator"], message, 2f)
                Konata.async.runnable {
                    val msgs = JsonArray()
                    val msgJson = JsonObject()
                    msgJson.addProperty("role", "user")
                    msgJson.addProperty("content", message)
                    msgs.add(msgJson)
                    val result = requestClientAI(
                        "You are a translator now, You need to translate following sentence to Chinese, you may use concepts in Minecraft",
                        "gpt-3.5-turbo",
                        msgs
                    )
                    if (result[0] == "200") {
                        Utility.sendClientMessage("${TextFormattingProvider.getGray()} ${result[1]}")
                    } else {
                        Utility.sendClientMessage("${TextFormattingProvider.getRed()} ${result[0]}: ${result[1]}")
                    }
                }
                e.cancel()
                return
            }
            if (msg.startsWith("#t ")) {
                val lang = msg.substring(0, msg.indexOf(" ")).replace("#", "")
                val message = msg.substring(msg.indexOf(" "), msg.length)
                NotificationManager.addNotification(Konata.i18n["translator"], "$lang: $message", 2f)
                Konata.async.runnable {
                    val openAi = OpenAi(
                        "https://one.aiskt.com/v1",
                        "sk-CwOF5Xs25FbSWPkK361b3574382346Ee9069Ae978b26Ee38",
                        "gpt-3.5-turbo-0125",
                        "You are a translator now, You need to translate following sentence to $lang. Please translate to $lang, Just answer the translation, dont add any extra things!"
                    )
                    val requestNewAnswer = openAi.requestNewAnswer(message)
                    if (ProviderManager.mcProvider.getPlayer() == null)
                        return@runnable
                    ProviderManager.mcProvider.getPlayer()?.sendChatMessage(requestNewAnswer.trim())
                }
                e.cancel()
            }
        }
    }

    @Subscribe
    fun onTick(e: EventTick) {
        if (time.delay(500)) {
            if (Minecraft.getMinecraft().currentScreen is MainMenu) {
                if (!MusicPlayer.playList.musics.isEmpty()) {
                    if (MusicPlayer.isPlaying) {
                        MusicPlayer.playList.pause()
                    }
                }
            }
        }

        if (musicSwitchTimer.delay(3000)) {
            if (MusicPlayer.isPlaying && MusicPlayer.playProgress > 0.999) {
                MusicPlayer.playList.next()
            }
        }
    }

    @Subscribe
    fun onRender(e: EventRender2D) {
        if (BlurBuffer.blurEnabled())
            BlurBuffer.updateBlurBuffer(true)

        val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
        val mouseX = Mouse.getX().toFloat() / scaledResolution.scaleFactor
        val mouseY = scaledResolution.scaledHeight - Mouse.getY().toFloat() / scaledResolution.scaleFactor
        Konata.componentsManager.draw(mouseX.toInt(), mouseY.toInt())
        NotificationManager.drawNotifications()
    }
}
