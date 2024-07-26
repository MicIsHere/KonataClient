package cn.cutemic.konata.websocket.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cn.cutemic.konata.websocket.data.message.Packet

val gson: Gson = GsonBuilder().create()
fun parseJson(json: String, clazz: Class<*>): Any {
    return gson.fromJson(json, clazz)
}

fun getRank(rank:String):String{
    when (rank) {
        "User" -> {
            return "§f用户§9"
        }
        "Beta" -> {
            return "§e赞助者§9"
        }
        "Promoter" -> {
            return "§2推广§9"
        }
        "Admin" -> {
            return "§l§6开发者§r§9"
        }
        else -> {
            return "§f用户§9"
        }
    }
}

fun toJson(packet: Packet): String {
    return gson.toJson(packet)
}
