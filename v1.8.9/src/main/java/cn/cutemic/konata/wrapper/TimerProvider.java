package cn.cutemic.konata.wrapper;

import net.minecraft.client.Minecraft;
import cn.cutemic.konata.forge.api.IMinecraft;
import cn.cutemic.konata.interfaces.ProviderManager;
import cn.cutemic.konata.interfaces.game.ITimerProvider;

public class TimerProvider implements ITimerProvider {
    public float getRenderPartialTicks(){
        return ((IMinecraft) Minecraft.getMinecraft()).arch$getTimer().renderPartialTicks;
    }
}
