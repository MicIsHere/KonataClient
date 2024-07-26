package cn.cutemic.konata.event.events

import net.minecraft.network.Packet
import cn.cutemic.konata.event.CancelableEvent

class EventPacket(@JvmField var type: PacketType, @JvmField var packet: Packet<*>) : CancelableEvent() {
    enum class PacketType {
        SEND,
        RECEIVE
    }
}
