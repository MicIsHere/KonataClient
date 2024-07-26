package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import cn.cutemic.konata.event.EventDispatcher;
import cn.cutemic.konata.event.events.EventUpdate;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "onLivingUpdate",at=@At("HEAD"))
    public void onUpdate(CallbackInfo ci){
        EventDispatcher.dispatchEvent(new EventUpdate());
    }
}
