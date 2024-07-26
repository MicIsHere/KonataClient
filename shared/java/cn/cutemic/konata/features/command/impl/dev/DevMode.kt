package cn.cutemic.konata.features.command.impl.dev

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.command.Command
import cn.cutemic.konata.utils.Utility

class DevMode :Command("dev") {
    override fun execute(args: Array<String>) {
        Konata.debug = !Konata.debug
        Utility.sendClientMessage("Dev mode is now ${if(Konata.debug) "enabled" else "disabled"}")
    }
}