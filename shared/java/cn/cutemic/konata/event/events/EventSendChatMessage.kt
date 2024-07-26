package cn.cutemic.konata.event.events

import cn.cutemic.konata.event.CancelableEvent

class EventSendChatMessage(@JvmField var msg: String) : CancelableEvent()
