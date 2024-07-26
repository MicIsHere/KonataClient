package cn.cutemic.konata.wrapper;

import net.minecraft.client.Minecraft;
import cn.cutemic.konata.forge.api.IRenderManager;
import cn.cutemic.konata.interfaces.render.IRenderManagerProvider;

public class RenderManagerProvider implements IRenderManagerProvider {
    public double renderPosX(){
        return ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosX();
    }

    public double renderPosY(){
        return ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosY();
    }

    public double renderPosZ(){
        return ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosZ();
    }
}
