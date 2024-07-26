package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerOverlay {

    Minecraft mc = Minecraft.getMinecraft();


    @Shadow
    public abstract String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn);

    @Shadow
    private IChatComponent header;

    @Shadow
    private IChatComponent footer;

    @Shadow
    protected abstract void drawScoreboardValues(ScoreObjective objective, int i, String name, int j, int k, NetworkPlayerInfo info);

    @Shadow
    protected abstract void drawPing(int i, int j, int k, NetworkPlayerInfo networkPlayerInfoIn);
}
