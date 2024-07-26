package cn.cutemic.konata.websocket.data.message.client;

import com.google.gson.annotations.SerializedName;
import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.PacketType;

public class FetchInfoPacket extends Packet {
    @SerializedName("type")
    public InfoType type;
    @SerializedName("data")
    public String data;

    public FetchInfoPacket(InfoType type, String data) {
        super(PacketType.CLIENT_FETCH);
        this.type = type;
    }
    public enum InfoType {
        CLIENT_PLAYER_INFO
    }
}
