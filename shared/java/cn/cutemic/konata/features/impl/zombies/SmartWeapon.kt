package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventMouseClick
import cn.cutemic.konata.event.events.EventTick
import cn.cutemic.konata.event.events.EventUpdate
import cn.cutemic.konata.features.impl.interfaces.Key
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ModeSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.ui.notification.NotificationManager
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.timing.TickTimer
import cn.cutemic.konata.utils.zombies.ZombiesUtils
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemHoe
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.network.play.client.C0CPacketInput
import net.minecraft.util.MovingObjectPosition
import org.lwjgl.input.Mouse

class SmartWeapon : Module("SmartWeapon", Category.Zombies) {
    private var autoSwitchMode = ModeSetting("SwitchMode", 1,"0", "1")
    private var rechangeCheck = BooleanSetting("Re-ChangingCheck", false)
    private var shotGunFirst = BooleanSetting("ShotGunFirst", false)
    private var shotGunFirstRange = NumberSetting("ShotGunFirstRange",2,0,6,0.1)
    private var delay = NumberSetting("Delay",10,100,1000,10)

    private val weaponList = mutableListOf<Int>()
    private var canEcoWeapon = mutableListOf<Int>()
    private var progress = 0
    private val timer = TickTimer()

    init {
        addSettings(autoSwitchMode, delay, rechangeCheck, shotGunFirst, shotGunFirstRange)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
        weaponList.clear()
        progress = 0
    }

    @Subscribe
    fun onUpdate(e: EventUpdate) {
        if (mc.thePlayer == null) {
            return
        }

        (0..8).forEach { number ->
            mc.thePlayer.inventory.mainInventory[number]?.let {
                if (ZombiesUtils.isWeapon(it.item) && weaponList.getOrNull(number) == null) {
                    weaponList.add(number)
                }
            }
        }

        canEcoWeapon = weaponList.filter { isEcoWeapon(mc.thePlayer.inventory.mainInventory[it].item) }.toMutableList()
    }

    @Subscribe
    fun onClick(e: EventMouseClick) {
        if (e.button != 1) {
            return
        }

        if (!ZombiesUtils.isWeapon(mc.thePlayer.heldItem.item)) {
            return
        }

        if (!timer.tickAndReset(delay.value.toInt())) {
            return
        }

        if (shotGunFirst.value) {
            val result0 = mc.objectMouseOver
            if (result0?.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                val result = result0.entityHit
                if (mc.thePlayer.getDistanceSqToEntity(result) <= shotGunFirstRange.value.toDouble() ) {
                    (0..8).forEach { number ->
                        mc.thePlayer.inventory.mainInventory[number]?.let {

                            if (it.item == Items.iron_hoe || it.item == Items.flint_and_steel) {
                                if (it.itemDamage != 0) {
                                    progress++
                                    if (autoSwitchMode.value == 0) {
                                        mc.thePlayer.inventory.currentItem = weaponList[progress]
                                    }
                                    mc.thePlayer.inventory.currentItem = canEcoWeapon[progress]
                                    return
                                }
                                NotificationManager.addNotification(Konata.i18n["smartweapon"],Konata.i18n["smartweapon.shotgunfirst.message"],2f)
                                mc.thePlayer.inventory.currentItem = number
                                return
                            }
                        }
                    }
                }
            }
        }

        when (autoSwitchMode.value) {
            0 -> {
                if (progress == weaponList.size) {
                    progress = 0
                }

                if (rechangeCheck.value) {
                    mc.thePlayer.inventory.mainInventory[weaponList[progress]]?.let {
                        if (it.itemDamage != 0) {
                            progress++
                        }
                    }
                }

                mc.thePlayer.inventory.currentItem = weaponList[progress]
                progress++
            }

            1 -> {
                if (progress == canEcoWeapon.size) {
                    progress = 0
                }

                if (rechangeCheck.value) {
                    mc.thePlayer.inventory.mainInventory[canEcoWeapon[progress]]?.let {
                        if (it.itemDamage != 0) {
                            progress++
                        }
                    }
                }

                mc.thePlayer.inventory.currentItem = canEcoWeapon[progress]
                progress++
            }
        }
    }

    private fun isEcoWeapon(item: Item?): Boolean{
        return when (item) {
            Items.wooden_hoe -> true
            Items.iron_hoe -> true
            else -> false
        }
    }

    companion object {
        var using = false
    }
}
