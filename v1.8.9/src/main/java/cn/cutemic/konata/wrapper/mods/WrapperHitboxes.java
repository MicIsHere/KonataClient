package cn.cutemic.konata.wrapper.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import cn.cutemic.konata.event.events.EventRender3D;
import cn.cutemic.konata.features.settings.impl.ColorSetting;
import cn.cutemic.konata.forge.api.IRenderManager;
import cn.cutemic.konata.interfaces.ProviderManager;
import cn.cutemic.konata.wrapper.RenderManagerProvider;
import cn.cutemic.konata.wrapper.TimerProvider;
import cn.cutemic.konata.wrapper.util.WrapperAxisAlignedBB;

import java.util.stream.Collectors;

public class WrapperHitboxes {
    public static void render(EventRender3D event, ColorSetting color) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(e -> e != Minecraft.getMinecraft().thePlayer && !e.isInvisible()).collect(Collectors.toList())) {
            AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ProviderManager.timerProvider.getRenderPartialTicks();
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ProviderManager.timerProvider.getRenderPartialTicks();
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ProviderManager.timerProvider.getRenderPartialTicks();
            double x = d0 - ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosX();
            double y = d1 - ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosY();
            double z = d2 - ((IRenderManager) Minecraft.getMinecraft().getRenderManager()).renderPosZ();
            AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entity.posX + x, axisalignedbb.minY - entity.posY + y, axisalignedbb.minZ - entity.posZ + z, axisalignedbb.maxX - entity.posX + x, axisalignedbb.maxY - entity.posY + y, axisalignedbb.maxZ - entity.posZ + z);
            RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue(), color.getColor().getAlpha());
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
}
