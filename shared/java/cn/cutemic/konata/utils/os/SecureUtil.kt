package cn.cutemic.konata.utils.os

import kotlin.system.exitProcess

object SecureUtil {
    val hwid: String?
        get() {
            val input =
                "sauce" + System.getProperty("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv(
                    "PROCESSOR_LEVEL"
                )
            return CryptUtils.getSHA256Hash(input)
        }

    fun crash() {
        exitProcess(0)
    }
}
