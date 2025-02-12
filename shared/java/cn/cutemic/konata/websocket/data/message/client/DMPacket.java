package cn.cutemic.konata.websocket.data.message.client;

import com.google.gson.annotations.SerializedName;
import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.PacketType;

public class DMPacket extends Packet {
    @SerializedName("to")
    public String to;
    @SerializedName("msg")
    public String msg;

    public DMPacket(String to, String msg) {
        super(PacketType.CLIENT_DIRECT_MSG);
        this.to = to;
        this.msg = msg;
    }
}
