package cn.cutemic.konata.interfaces.client

interface IConstantsProvider {
    fun getVersion(): String
    fun getEdition(): String
}