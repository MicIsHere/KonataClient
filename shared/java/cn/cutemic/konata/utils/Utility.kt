package cn.cutemic.konata.utils

import net.minecraft.client.Minecraft
import cn.cutemic.konata.interfaces.ProviderManager

open class Utility {
    companion object {
        @JvmField
        var mc: Minecraft = Minecraft.getMinecraft()

        @JvmStatic
        fun sendClientMessage(msg: String?) {
            if (ProviderManager.mcProvider.getWorld() != null) {
                ProviderManager.mcProvider.printChatMessage(ProviderManager.utilityProvider.makeChatComponent(msg))
            }
        }
    }
}
