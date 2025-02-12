package cn.cutemic.konata.forge.mixin;

import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import cn.cutemic.konata.event.EventDispatcher;
import cn.cutemic.konata.event.events.EventMotionBlur;

@Mixin(GuiIngameForge.class)
public class MixinGuiIngameForge {
    @Inject(method = "renderGameOverlay",at = @At("RETURN"))
    public void motionblur(float partialTicks, CallbackInfo ci){
        EventDispatcher.dispatchEvent(new EventMotionBlur());
    }
}
