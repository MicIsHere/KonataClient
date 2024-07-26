package cn.cutemic.konata.interfaces.game

import net.minecraft.client.settings.KeyBinding
import cn.cutemic.konata.interfaces.IProvider

interface IGameSettings: IProvider {
    fun setKeyPress(key: KeyBinding, value: Boolean)
}