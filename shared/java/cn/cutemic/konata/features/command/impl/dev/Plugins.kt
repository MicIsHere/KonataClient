package cn.cutemic.konata.features.command.impl.dev

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.command.Command
import cn.cutemic.konata.modules.plugin.PluginManager
import cn.cutemic.konata.utils.Utility

class Plugins: Command("plugins"){
    override fun execute(args: Array<String>) {
        if (args.isEmpty())
            Utility.sendClientMessage("Usage: #plugins <list|reload>")
        else
            if (args[0] == "list"){
                for (plugin in PluginManager.plugins) {
                    Utility.sendClientMessage(plugin.javaClass.simpleName)
                }
            }else if (args[0] == "reload"){
                Konata.plugins.reload()
            }else{
                Utility.sendClientMessage("Usage: #plugins <list|reload>")
            }
    }
}