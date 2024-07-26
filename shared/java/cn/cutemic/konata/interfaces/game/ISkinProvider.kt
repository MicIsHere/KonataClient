package cn.cutemic.konata.interfaces.game

import cn.cutemic.konata.interfaces.IProvider

interface ISkinProvider : IProvider {
    fun updateSkin(name: String?, uuid: String?, skin: String?)
}