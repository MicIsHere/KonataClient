package cn.cutemic.konata.wrapper.packets;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import org.jetbrains.annotations.NotNull;
import cn.cutemic.konata.interfaces.packets.IAddPlayerData;
import cn.cutemic.konata.interfaces.packets.IPacketPlayerList;

import java.util.List;
import java.util.stream.Collectors;

public class SPacketPlayerListProvider implements IPacketPlayerList {
    public boolean isPacket(@NotNull Object p) {
        return p instanceof S38PacketPlayerListItem;
    }

    public boolean isActionJoin(@NotNull Object p) {
        return isPacket(p) && ((S38PacketPlayerListItem) p).getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER;
    }

    public boolean isActionLeave(@NotNull Object p) {
        return isPacket(p) && ((S38PacketPlayerListItem) p).getAction() == S38PacketPlayerListItem.Action.REMOVE_PLAYER;
    }

    public List<IAddPlayerData> getEntries(@NotNull Object p) {
        List<S38PacketPlayerListItem.AddPlayerData> entries = ((S38PacketPlayerListItem) p).getEntries();
        return entries.stream().map(WrapperAddPlayerData::new).collect(Collectors.toList());
    }

}

