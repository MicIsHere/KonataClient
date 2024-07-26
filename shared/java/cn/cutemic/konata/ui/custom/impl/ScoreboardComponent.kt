package cn.cutemic.konata.ui.custom.impl

import cn.cutemic.konata.features.impl.interfaces.Scoreboard
import cn.cutemic.konata.ui.custom.Component
import cn.cutemic.konata.wrapper.mods.WrapperScoreboard

class ScoreboardComponent : Component(Scoreboard::class.java) {
    override fun draw(x: Float, y: Float) {
        super.draw(x, y)
        val render = WrapperScoreboard.render(this, mod, x, y)
        width = render[0]
        height = render[1]
    }
}
