package cn.cutemic.konata.interfaces

import cn.cutemic.konata.interfaces.client.IConstantsProvider
import cn.cutemic.konata.interfaces.game.*
import cn.cutemic.konata.interfaces.gui.IGuiIngameProvider
import cn.cutemic.konata.interfaces.gui.IGuiMainMenuProvider
import cn.cutemic.konata.interfaces.packets.IPacketChat
import cn.cutemic.konata.interfaces.packets.IPacketPlayerList
import cn.cutemic.konata.interfaces.packets.IPacketTimeUpdate
import cn.cutemic.konata.interfaces.render.IRenderManagerProvider
import cn.cutemic.konata.wrapper.*
import cn.cutemic.konata.wrapper.packets.SPacketChatProvider
import cn.cutemic.konata.wrapper.packets.SPacketPlayerListProvider
import cn.cutemic.konata.wrapper.packets.SPacketTimeUpdateProvider

object ProviderManager {
    @JvmField
    val constants: IConstantsProvider = Constants()
    @JvmField
    var utilityProvider: IUtilityProvider = UtilityProvider()
    @JvmField
    val mcProvider: IMinecraftProvider = MinecraftProvider()
    @JvmField
    val mainmenuProvider: IGuiMainMenuProvider = GuiMainMenuProvider()
    @JvmField
    val skinProvider: ISkinProvider = SkinProvider()
    @JvmField
    val worldClientProvider: IWorldClientProvider = WorldClientProvider()
    @JvmField
    val timerProvider: ITimerProvider = TimerProvider()
    @JvmField
    val renderManagerProvider: IRenderManagerProvider = RenderManagerProvider()
    @JvmField
    val gameSettings: IGameSettings = GameSettingsProvider()

    // Packets
    @JvmField
    val packetChat: IPacketChat = SPacketChatProvider()
    @JvmField
    val packetPlayerList: IPacketPlayerList = SPacketPlayerListProvider()
    @JvmField
    val packetTimeUpdate: IPacketTimeUpdate = SPacketTimeUpdateProvider()
    @JvmField
    val guiIngameProvider: IGuiIngameProvider = GuiIngameProvider()

}
