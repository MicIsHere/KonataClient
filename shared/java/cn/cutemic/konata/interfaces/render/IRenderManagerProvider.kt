package cn.cutemic.konata.interfaces.render

import cn.cutemic.konata.interfaces.IProvider

interface IRenderManagerProvider : IProvider {
    fun renderPosX(): Double
    fun renderPosY(): Double
    fun renderPosZ(): Double
}