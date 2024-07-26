package cn.cutemic.konata.event.events

import net.minecraft.network.Packet
import cn.cutemic.konata.event.Event

class EventCustomPacket(var packet: Packet<*>) : Event
