package cn.cutemic.konata.websocket.data.message.client;

import com.google.gson.annotations.SerializedName;
import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.PacketType;

public class ServerInfoPacket extends Packet {

    @SerializedName("serverIP")
    public String serverIP;

    public ServerInfoPacket(String serverIP) {
        super(PacketType.CLIENT_SERVER_INFO);
        this.serverIP = serverIP;
    }
}
