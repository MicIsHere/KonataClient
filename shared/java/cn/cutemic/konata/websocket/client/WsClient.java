package cn.cutemic.konata.websocket.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import cn.cutemic.konata.Konata;
import cn.cutemic.konata.interfaces.ProviderManager;
import cn.cutemic.konata.utils.Utility;
import cn.cutemic.konata.websocket.data.message.Packet;
import cn.cutemic.konata.websocket.data.message.client.*;
import cn.cutemic.konata.websocket.data.message.server.SDataPacket;
import cn.cutemic.konata.websocket.data.message.server.SMessagePacket;

import java.net.URI;
import java.net.URISyntaxException;

public class WsClient extends WebSocketClient {

    public WsClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (Konata.debug)
            Utility.sendClientMessage("成功连接到irc服务器");
        if (ProviderManager.mcProvider.getPlayer() != null) {
            Utility.sendClientMessage(Konata.i18n.get("irc.enable"));
        }
        assert Konata.accountManager != null;
        send(new LoginPacket(Konata.accountManager.getUsername(), Konata.accountManager.getToken()).toJson());
    }

    public void sendMessage(String message) {
        send(new MessagePacket(MessagePacket.MessageType.CHAT, message).toJson());
    }

    public void sendInformation(String skin, String cape, String gameID, String serverAddress) {
        if (ProviderManager.mcProvider.getPlayer() == null)
            return;
        send(new PlayerInfoPacket(gameID, ProviderManager.mcProvider.getPlayer().getUniqueID().toString()).toJson());
        send(new ServerInfoPacket(serverAddress).toJson());
        send(new CosmeticInfoPacket(skin, cape).toJson());
    }


    public void sendCommand(String message) {
        send(new MessagePacket(MessagePacket.MessageType.COMMAND, message).toJson());
    }

    public void sendDM(String to, String message) {
        send(new DMPacket(to, message).toJson());
    }

    @Override
    public void onMessage(String message) {
        Packet packet = Packet.parsePacket(message, Packet.class);
        switch (packet.type) {
            case SERVER_MESSAGE:
                SMessagePacket parsePacket = (SMessagePacket) Packet.parsePacket(message, SMessagePacket.class);
                Utility.sendClientMessage(parsePacket.msg);
                break;
            case SERVER_DATA:
                SDataPacket packet1 = (SDataPacket) packet;
                String data = packet1.data;
                JsonObject asJsonObject = new JsonParser().parse(data).getAsJsonObject();
                break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (Konata.debug)
            Utility.sendClientMessage("连接关闭:" + reason);
        Konata.INSTANCE.setWsClient(null);
    }

    @Override
    public void onError(Exception ex) {
        if (Konata.debug)
            Utility.sendClientMessage("聊天服务错误: " + ex.getMessage());
    }

    public static WsClient start(String addr) throws URISyntaxException {
        WsClient client = new WsClient(new URI(addr));
        client.connect();
        return client;
    }
}