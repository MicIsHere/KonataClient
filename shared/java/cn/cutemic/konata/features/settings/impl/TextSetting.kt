package cn.cutemic.konata.features.settings.impl

import cn.cutemic.konata.features.settings.Setting

class TextSetting(name: String, value: String) : Setting<String>(name, value){
    constructor(name: String, value: String, visible: () -> Boolean) : this(name, value) {
        this.visible = visible
    }
}