package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import cn.cutemic.konata.ui.screens.mainmenu.MainMenu;

@Mixin(GuiMainMenu.class)
public class MixinMainMenu {

    /**
     * @author SuperSkidder
     * @reason replace
     */
    @Overwrite
    public void initGui(){
        Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
    }
}
