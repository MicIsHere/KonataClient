package cn.cutemic.konata.wrapper.packets;

import net.minecraft.network.play.server.S38PacketPlayerListItem;
import org.jetbrains.annotations.NotNull;
import cn.cutemic.konata.interfaces.packets.IAddPlayerData;

public class WrapperAddPlayerData implements IAddPlayerData {
    public S38PacketPlayerListItem.AddPlayerData entity;

    public WrapperAddPlayerData(S38PacketPlayerListItem.AddPlayerData entity) {
        this.entity = entity;
    }

    @NotNull
    public String getName() {
        return entity.getProfile().getName();
    }
}
