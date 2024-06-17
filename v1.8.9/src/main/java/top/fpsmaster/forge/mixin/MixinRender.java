package top.fpsmaster.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fpsmaster.FPSMaster;
import top.fpsmaster.features.impl.optimizes.Performance;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static top.fpsmaster.utils.Utility.mc;
import static top.fpsmaster.utils.render.Render2DUtils.drawModalRectWithCustomSizedTexture;

@Mixin(Render.class)
public abstract class MixinRender {
    protected MixinRender() {
    }

    @Shadow
    protected abstract boolean bindEntityTexture(Entity entity);

    @Shadow
    public abstract void bindTexture(ResourceLocation location);

    @Final
    @Shadow
    protected RenderManager renderManager;
    @Shadow
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks){}

    @Inject(method = "renderName",at = @At("HEAD"), cancellable = true)
    public void ignore(Entity entity, double x, double y, double z, CallbackInfo ci){
        if (Performance.using && Performance.ignoreStands.getValue() && entity instanceof EntityArmorStand) {
            ci.cancel();
        }
    }

}
