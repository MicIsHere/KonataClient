package cn.cutemic.konata.features.command

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.EventDispatcher
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventSendChatMessage
import cn.cutemic.konata.features.command.impl.chat.IRCChat
import cn.cutemic.konata.features.command.impl.dev.DevMode
import cn.cutemic.konata.features.command.impl.dev.Plugins
import cn.cutemic.konata.features.impl.utility.ClientCommand
import cn.cutemic.konata.utils.Utility

class CommandManager {
    private val commands = mutableListOf<Command>()

    fun init() {
        // add commands
        commands.add(DevMode())
        commands.add(Plugins())
        commands.add(IRCChat())
        EventDispatcher.registerListener(this)
    }

    @Subscribe
    fun onChat(e: EventSendChatMessage){
        if(ClientCommand.using && e.msg.startsWith(ClientCommand.prefix.value)){
            e.cancel()
            try {
                runCommand(e.msg.substring(1))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun runCommand(command: String){
        val args = command.split(" ")
        val cmd = args[0]
        if (args.size == 1){
            commands.forEach {
                if(it.name == cmd){
                    it.execute(arrayOf())
                    return
                }
            }
            Utility.sendClientMessage(Konata.i18n["command.notfound"])
            return
        }
        val cmdArgs = args.subList(1, args.size).toTypedArray()
        commands.forEach {
            if(it.name == cmd){
                it.execute(cmdArgs)
                return
            }
        }
        Utility.sendClientMessage(Konata.i18n["command.notfound"])
    }
}