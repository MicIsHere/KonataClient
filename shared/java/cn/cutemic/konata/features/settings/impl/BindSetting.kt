package cn.cutemic.konata.features.settings.impl

import cn.cutemic.konata.features.settings.Setting

class BindSetting(name: String, value: Int) : Setting<Int>(name, value){
    constructor(name: String, value: Int, visible: () -> Boolean) : this(name, value) {
        this.visible = visible
    }

}
