package cn.cutemic.konata.modules.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import cn.cutemic.konata.Konata
import cn.cutemic.konata.features.impl.optimizes.OldAnimations
import cn.cutemic.konata.features.impl.optimizes.Performance
import cn.cutemic.konata.features.impl.render.ItemPhysics
import cn.cutemic.konata.features.impl.utility.ClientCommand
import cn.cutemic.konata.features.impl.utility.IRC
import cn.cutemic.konata.features.settings.impl.*
import cn.cutemic.konata.ui.custom.Position
import cn.cutemic.konata.utils.os.FileUtils
import cn.cutemic.konata.utils.os.FileUtils.readFile
import cn.cutemic.konata.utils.os.FileUtils.saveFile

class ConfigManager {
    var gson: Gson = GsonBuilder().setPrettyPrinting().create()

    @JvmField
    var configure = Configure()

    init {
        if (!FileUtils.dir.exists()) FileUtils.dir.mkdirs()
    }


    private fun saveComponents() {
        val json = JsonObject().apply {
            for (module in Konata.componentsManager.components) {
                val component = JsonObject().apply {
                    addProperty("name", module.mod.name)
                    addProperty("x", module.x)
                    addProperty("y", module.y)
                    addProperty("position", module.position.name)
                }
                add(module.mod.name, component)
            }
        }
        saveFile("components.json", gson.toJson(json))
    }

    private fun readComponents() {
        val jsonStr = readFile("components.json")
        if (jsonStr.isEmpty()) return
        val json = gson.fromJson(jsonStr, JsonObject::class.java)
        for (mod in Konata.moduleManager.modules) {
            json.getAsJsonObject(mod.name)?.let { module ->
                Konata.componentsManager.components.find { it.mod.name == module["name"].asString }?.apply {
                    x = module["x"].asFloat
                    y = module["y"].asFloat
                    position = Position.valueOf(module["position"].asString)
                }
            }
        }
    }


    fun saveConfig(name: String) {
        saveComponents()
        val json = JsonObject().apply {
            addProperty("theme", Konata.themeSlot)
            addProperty("clientConfigure", gson.toJson(configure.configures))
            for (module in Konata.moduleManager.modules) {
                val moduleJson = JsonObject().apply {
                    addProperty("enabled", module.isEnabled)
                    addProperty("key", module.key)
                    for (setting in module.settings) {
                        addProperty(
                            setting.name, when (setting) {
                                is ColorSetting -> "${setting.value.hue}|${setting.value.saturation}|${setting.value.brightness}|${setting.value.alpha}"
                                else -> setting.value.toString()
                            }
                        )
                    }
                }
                add(module.name, moduleJson)
            }
        }
        saveFile("$name.json", gson.toJson(json))
    }

    fun loadConfig(name: String) {
        try {
            var jsonStr = readFile("$name.json")
            if (jsonStr.isEmpty()) {
                openDefaultModules()
                saveConfig("default")
                loadConfig(name)
                return
            }
            readComponents()
            jsonStr = readFile("$name.json")
            val json = gson.fromJson(jsonStr, JsonObject::class.java)
            Konata.themeSlot = json["theme"].asString
            for (module in Konata.moduleManager.modules) {
                json.getAsJsonObject(module.name)?.let { moduleJson ->
                    module.set(moduleJson["enabled"].asBoolean)
                    module.key = moduleJson["key"].asInt
                    for (setting in module.settings) {
                        moduleJson[setting.name]?.let { settingValue ->
                            when (setting) {
                                is BooleanSetting -> setting.value = settingValue.asBoolean
                                is NumberSetting -> setting.value = settingValue.asDouble
                                is ModeSetting -> setting.value = settingValue.asInt
                                is TextSetting -> setting.value = settingValue.asString
                                is ColorSetting -> settingValue.asString.split("|").map { it.toFloat() }.let {
                                    setting.value.setColor(it[0], it[1], it[2], it[3])
                                }

                                is BindSetting -> setting.value = settingValue.asInt
                            }
                        }
                    }
                }
            }
            configure.configures =
                gson.fromJson(json["clientConfigure"].asString, HashMap::class.java) as HashMap<String, String>
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openDefaultModules() {
        with(Konata.moduleManager) {
            getModule(Performance::class.java).set(true)
            getModule(OldAnimations::class.java).set(true)
            getModule(ItemPhysics::class.java).set(true)
            getModule(ClientCommand::class.java).set(true)
        }
    }
}
