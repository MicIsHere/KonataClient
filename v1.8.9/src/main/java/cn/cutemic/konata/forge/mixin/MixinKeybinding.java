package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import cn.cutemic.konata.forge.api.IKeyBinding;

@Mixin(KeyBinding.class)
@Implements(@Interface(iface = IKeyBinding.class, prefix = "konata$"))
public class MixinKeybinding implements IKeyBinding {

    @Shadow
    private boolean pressed;

    @Override
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
