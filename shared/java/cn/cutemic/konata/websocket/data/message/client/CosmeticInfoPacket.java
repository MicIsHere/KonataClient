package cn.cutemic.konata.websocket.data.message.client;

import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.PacketType;

public class CosmeticInfoPacket extends Packet {
    public String skin;
    public String cape;

    public CosmeticInfoPacket(String skin, String cape) {
        super(PacketType.CLIENT_COSMETIC_INFO);
        this.skin = skin;
        this.cape = cape;
    }
}
