package cn.cutemic.konata.utils

import net.minecraft.client.settings.GameSettings
import cn.cutemic.konata.Konata
import java.lang.reflect.Field

class OptifineUtil : Utility() {
    companion object {
        private var ofFastRender: Field? = null

        fun isFastRender(): Boolean {
            if (!Konata.INSTANCE.hasOptifine) return false
            try {
                if (ofFastRender == null) {
                    Class.forName("Config")
                    ofFastRender = GameSettings::class.java.getDeclaredField("ofFastRender")
                }
                ofFastRender!!.setAccessible(true)
                return ofFastRender!!.getBoolean(mc.gameSettings)
            } catch (ignore: ClassNotFoundException) {
            } catch (ignore: IllegalAccessException) {
            } catch (ignore: NoSuchFieldException) {
            }
            return false
        }

        fun setFastRender(value: Boolean) {
            if (!Konata.INSTANCE.hasOptifine) return
            try {
                if (ofFastRender == null) {
                    Class.forName("Config")
                    ofFastRender = GameSettings::class.java.getDeclaredField("ofFastRender")
                }
                ofFastRender!!.setAccessible(true)
                ofFastRender!!.setBoolean(mc.gameSettings, value)
            } catch (ignore: ClassNotFoundException) {
            } catch (ignore: IllegalAccessException) {
            } catch (ignore: NoSuchFieldException) {
            }
        }
    }
}