package cn.cutemic.konata.utils.thirdparty.github

import org.json.JSONObject
import cn.cutemic.konata.utils.os.HttpRequest

object UpdateChecker {
    fun getLatestVersion(): String? {
        val json =
            HttpRequest["https://api.github.com/repos/FPSMasterTeam/FPSMaster/releases/latest"]
        val jsonObject = JSONObject(json)
        return jsonObject.getString("tag_name")
    }
}