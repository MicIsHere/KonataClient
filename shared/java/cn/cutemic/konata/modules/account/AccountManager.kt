package cn.cutemic.konata.modules.account

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import cn.cutemic.konata.Konata
import cn.cutemic.konata.modules.logger.Logger
import cn.cutemic.konata.utils.os.FileUtils
import cn.cutemic.konata.utils.os.HttpRequest

class AccountManager {
    var token: String = ""
    var username: String = ""
    var itemsHeld = emptyArray<String>()

    fun autoLogin() {
        try {
            token = FileUtils.readTempValue("token").trim()
            username = Konata.configManager.configure.getOrCreate("username", "offline").trim()
            if (token.isNotEmpty() && username.isNotEmpty()) {
                if (attemptLogin(username, token)) {
                    Logger.info("自动登录成功！  $username")
                    Konata.INSTANCE.loggedIn = true
                    getItems(username, token)
                } else {
                    Logger.info(username)
                    Logger.error("自动登录失败！")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.error("尝试自动登录失败！${e.message}")
        }
    }

    private fun attemptLogin(username: String, token: String): Boolean {
        if (username.isEmpty() || token.isEmpty())
            return false
        val s =
            HttpRequest["${Konata.SERVICE_API}/checkToken?username=$username&token=$token&timestamp=${System.currentTimeMillis()}"]
        val json = parser.parse(s).getAsJsonObject()
        this.username = username
        this.token = token
        return json["code"].asInt == 200
    }

    fun getItems(username: String?, token: String?) {
        val s =
            HttpRequest["${Konata.SERVICE_API}/getWebUser?username=$username&token=$token&timestamp=${System.currentTimeMillis()}"]
        val json = parser.parse(s).getAsJsonObject()
        if (json["code"].asInt == 200) {
            val items = json["data"].getAsJsonObject()["items"].asString
            itemsHeld = items.split(",").filter { it.isNotEmpty() }.toTypedArray()
        }
    }

    companion object {
        var parser = JsonParser()
        var cape = ""
        var skin = ""

        fun login(username: String, password: String): JsonObject {
            val s =
                HttpRequest["${Konata.SERVICE_API}/login?username=$username&password=$password&timestamp=${System.currentTimeMillis()}"]
            return parser.parse(s).getAsJsonObject()
        }
    }
}
