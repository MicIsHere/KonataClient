package cn.cutemic.konata.features.impl.interfaces

import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.InterfaceModule
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.ui.notification.NotificationManager
import cn.cutemic.konata.utils.OptifineUtil

class MiniMap : InterfaceModule("MiniMap", Category.Interface) {
    override fun onEnable() {
        super.onEnable()
        if (OptifineUtil.isFastRender()) {
            OptifineUtil.setFastRender(false)
            NotificationManager.addNotification(
                Konata.i18n["minimap.fastrender.disable.title"], Konata.i18n["minimap.fastrender.disable.title"],
                5000f
            )
        }
    }
}