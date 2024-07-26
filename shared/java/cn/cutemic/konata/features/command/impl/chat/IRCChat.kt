package cn.cutemic.konata.features.command.impl.chat

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.command.Command
import cn.cutemic.konata.utils.Utility

class IRCChat : Command("irc") {
    override fun execute(args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0] == "cmd") {
                val sb = StringBuilder()
                for (arg in args.drop(1)) {
                    if (arg == args.last())
                        sb.append(arg)
                    else
                        sb.append("$arg ")
                }
                val message = sb.toString()
                Konata.INSTANCE.wsClient!!.sendCommand(message)
            } else if (args[0] == "dm") {
                val sb = StringBuilder()
                for (arg in args.drop(2)) {
                    if (arg == args.last())
                        sb.append(arg)
                    else
                        sb.append("$arg ")
                }
                val message = sb.toString()
                Konata.INSTANCE.wsClient!!.sendDM(args[1], message)
            } else {
                val sb = StringBuilder()
                for (arg in args) {
                    if (arg == args.last())
                        sb.append(arg)
                    else
                        sb.append("$arg ")
                }
                val message = sb.toString()
                Konata.INSTANCE.wsClient!!.sendMessage(message)
            }
        } else {
            Utility.sendClientMessage("Usage: #irc <message>")
        }
    }
}