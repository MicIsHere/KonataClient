package cn.cutemic.konata.websocket.data.message.server;

import com.google.gson.annotations.SerializedName;
import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.PacketType;
import cn.cutemic.konata.websocket.data.message.client.FetchInfoPacket;

public class SDataPacket extends Packet {
    @SerializedName("type")
    public FetchInfoPacket.InfoType type;
    @SerializedName("data")
    public String data;

    public SDataPacket(FetchInfoPacket.InfoType type, String data) {
        super(PacketType.SERVER_DATA);
        this.type = type;
        this.data = data;
    }
}
