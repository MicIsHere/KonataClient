package cn.cutemic.konata.interfaces.game

import cn.cutemic.konata.interfaces.IProvider

interface ITimerProvider : IProvider {
    fun getRenderPartialTicks(): Float
}