package cn.cutemic.konata.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import cn.cutemic.konata.Konata;

@net.minecraftforge.fml.common.Mod(modid = "konata", useMetadata=true)
public class Mod {
    @net.minecraftforge.fml.common.Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventAPI());
        Konata.INSTANCE.initialize();
    }
}
