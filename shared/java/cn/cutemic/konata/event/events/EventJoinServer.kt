package cn.cutemic.konata.event.events

import cn.cutemic.konata.event.Event

class EventJoinServer(val serverId: String) : Event {
    var cancel: Boolean = false
}