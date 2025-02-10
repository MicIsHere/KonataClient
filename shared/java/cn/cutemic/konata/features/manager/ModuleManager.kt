package cn.cutemic.konata.features.manager

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import cn.cutemic.konata.event.EventDispatcher.registerListener
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.*
import cn.cutemic.konata.modules.logger.Logger.warn
import cn.cutemic.konata.features.impl.interfaces.*
import cn.cutemic.konata.features.impl.optimizes.*
import cn.cutemic.konata.features.impl.render.*
import cn.cutemic.konata.features.impl.utility.*
import cn.cutemic.konata.features.impl.zombies.*
import cn.cutemic.konata.interfaces.ProviderManager
import cn.cutemic.konata.ui.click.MainPanel
import java.util.*

class ModuleManager {
    var modules = LinkedList<Module>()
    private val mainPanel: GuiScreen = MainPanel(true)
    fun getModule(mod: Class<*>): Module {
        for (module in modules) {
            if (module.javaClass == mod) {
                return module
            }
        }
        warn("Missing module:" + mod.name)
        return Module("missing module", "mission", Category.Utility)
    }

    @Subscribe
    fun onKey(e: EventKey) {
        if (e.key == ClickGui.keyBind.value) if (Minecraft.getMinecraft().currentScreen == null) Minecraft.getMinecraft()
            .displayGuiScreen(mainPanel)
        for (module in modules) {
            if (e.key == module.key) {
                module.toggle()
            }
        }
    }

    fun init() {

        // register listener
        registerListener(this)
        // add mods
        modules.add(ClickGui())
        modules.add(BetterScreen())
        modules.add(Sprint())
        modules.add(Performance())
        modules.add(MotionBlur())
        modules.add(SmoothZoom())
        modules.add(FullBright())
        modules.add(ItemPhysics())
        modules.add(MinimizedBobbing())
        modules.add(MoreParticles())
        modules.add(FPSDisplay())
        modules.add(ArmorDisplay())
        modules.add(BetterChat())
        modules.add(ComboDisplay())
        modules.add(CPSDisplay())
        modules.add(PotionDisplay())
        modules.add(ReachDisplay())
        modules.add(Scoreboard())
        modules.add(MusicOverlay())
        modules.add(OldAnimations())
        modules.add(HitColor())
        modules.add(BlockOverlay())
        modules.add(DragonWings())
        modules.add(FireModifier())
        modules.add(FreeLook())
        modules.add(LyricsDisplay())
        modules.add(SkinChanger())
        modules.add(TimeChanger())
        modules.add(TNTTimer())
        modules.add(Hitboxes())
        modules.add(LevelTag())
        modules.add(Keystrokes())
        modules.add(Crosshair())
        modules.add(CustomFOV())
//        modules.add(TabOverlay())
        modules.add(InventoryDisplay())
        modules.add(PlayerDisplay())
        modules.add(TargetDisplay())
        modules.add(NoHurtCam())
        modules.add(NameProtect())
//        modules.add(ChatBot())
//        modules.add(Translator())
        modules.add(RawInput())
        modules.add(FixedInventory())
        modules.add(NoHitDelay())
        modules.add(PingDisplay())
        modules.add(CoordsDisplay())
        modules.add(ModsList())
        modules.add(ClientCommand())
        modules.add(MiniMap())
        modules.add(DirectionDisplay())

        //Zombies
        modules.add(TheOldOneCheck())
        modules.add(AutoHypixel())
        modules.add(HidePlayer())
        modules.add(SmartWeapon())
        modules.add(AntiWeaponLag())

        if (ProviderManager.constants.getVersion() == "1.12.2") {
            modules.add(HideIndicator())
        }
    }
}
