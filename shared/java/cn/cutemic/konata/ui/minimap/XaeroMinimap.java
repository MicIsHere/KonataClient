package cn.cutemic.konata.ui.minimap;

import net.minecraft.client.Minecraft;
import cn.cutemic.konata.ui.minimap.interfaces.InterfaceHandler;

import java.io.IOException;

public class XaeroMinimap
{
    public static XaeroMinimap instance;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public void load() throws IOException {
        InterfaceHandler.loadPresets();
        InterfaceHandler.load();
    }
}
