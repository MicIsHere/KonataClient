package cn.cutemic.konata.modules.plugin.api.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import org.lwjgl.input.Mouse;
import cn.cutemic.konata.Konata;
import cn.cutemic.konata.features.manager.Module;
import cn.cutemic.konata.interfaces.ProviderManager;
import cn.cutemic.konata.modules.plugin.api.client.PluginScreen;
import cn.cutemic.konata.modules.plugin.api.client.PluginGuiButton;
import cn.cutemic.konata.ui.screens.mainmenu.MainMenu;
import cn.cutemic.konata.utils.PluginGui;
import cn.cutemic.konata.utils.Utility;
import cn.cutemic.konata.utils.render.Render2DUtils;

import java.util.HashMap;

public class Util {
    public static HashMap<String, PluginGuiButton> buttons = new HashMap<>();

    public static float getScreenWidth() {
        ScaledResolution sr = new ScaledResolution(Utility.mc);
        return sr.getScaledWidth();
    }

    public static float getScreenHeight() {
        ScaledResolution sr = new ScaledResolution(Utility.mc);
        return sr.getScaledHeight();
    }

    public static void displayMainMenu() {
        Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
    }

    public static void displayConnecting(PluginScreen parent, String name, String ip) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new PluginGui(parent), Minecraft.getMinecraft(), new ServerData(name, ip, false)));
    }

    public static String getI18n(String text) {
        return Konata.i18n.get(text);
    }

    public static void drawOptimizedRect(float x, float y, float width, float height, int color) {
        Render2DUtils.drawOptimizedRoundedRect(x, y, width, height, color);
    }

    public static void drawRect(float x, float y, float width, float height, int color) {
        Render2DUtils.drawRect(x, y, width, height, color);
    }

    public static void displayScreen(PluginScreen screen) {
        Minecraft.getMinecraft().displayGuiScreen(new PluginGui(screen));
    }


    public static void setSession(String username, String playerID, String token, String type) {
        ProviderManager.mcProvider.setSession(new Session(username, playerID, token, type));
    }

    public static String getClientUserName() {
        assert Konata.accountManager != null;
        return Konata.accountManager.getUsername();
    }

    public static boolean isMouseDown(int b) {
        return Mouse.isButtonDown(b);
    }

    public static void openModule(String mod, boolean state) {
        for (Module module : Konata.moduleManager.getModules()) {
            if (module.getName().equals(mod)) {
                module.set(state);
            }
        }
    }

    public static String getClientUserToken() {
        assert Konata.accountManager != null;
        return Konata.accountManager.getToken();
    }
}
