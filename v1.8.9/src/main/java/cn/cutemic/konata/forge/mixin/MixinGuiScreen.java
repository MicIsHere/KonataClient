package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import cn.cutemic.konata.event.EventDispatcher;
import cn.cutemic.konata.event.events.EventSendChatMessage;
import cn.cutemic.konata.features.impl.interfaces.BetterScreen;
import cn.cutemic.konata.interfaces.ProviderManager;
import cn.cutemic.konata.utils.math.animation.AnimationUtils;
import cn.cutemic.konata.utils.render.Render2DUtils;

import java.io.IOException;

import static cn.cutemic.konata.utils.Utility.mc;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui {

    @Shadow
    protected abstract void keyTyped(char typedChar, int keyCode);

    @Shadow public int width;

    @Shadow public int height;

    @Shadow public abstract void drawBackground(int tint);

    /**
     * @author SuperSkidder
     * @reason 中文输入
     */
    @Overwrite
    public void handleKeyboardInput() throws IOException {
        char c0 = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            this.keyTyped(c0, Keyboard.getEventKey());
        }

        mc.dispatchKeypresses();
    }

    @Unique
    float arch$alpha = 0;

    /**
     * @author SuperSkidder
     * @reason 自定义背景
     */
    @Overwrite
    public void drawWorldBackground(int tint) {
        if (ProviderManager.mcProvider.getWorld() != null) {
            if (BetterScreen.using) {
                if (BetterScreen.useBG.getValue()) {
                    if (BetterScreen.backgroundAnimation.getValue()) {
                        arch$alpha = (float) AnimationUtils.base(arch$alpha, 170, 0.2f);
                    } else {
                        arch$alpha = 170;
                    }
                    this.drawGradientRect(0, 0, this.width, this.height, Render2DUtils.reAlpha(Render2DUtils.intToColor(-1072689136), ((int) arch$alpha)).getRGB(), Render2DUtils.reAlpha(Render2DUtils.intToColor(-804253680), ((int) arch$alpha)).getRGB());
                }
            } else {
                this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
            }
        } else {
            this.drawBackground(tint);
        }
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendChat(String msg, boolean addToChat, CallbackInfo ci){
        EventSendChatMessage eventSendChatMessage = new EventSendChatMessage(msg);
        EventDispatcher.dispatchEvent(eventSendChatMessage);
        if (eventSendChatMessage.isCanceled()) {
            ci.cancel();
        }
    }
}
