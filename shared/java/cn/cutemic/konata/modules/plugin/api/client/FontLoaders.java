package cn.cutemic.konata.modules.plugin.api.client;

import cn.cutemic.konata.Konata;

public class FontLoaders {
    public static void drawString(int size, String text, float x, float y, int color, boolean shadow) {
        assert Konata.fontManager != null;
        if (shadow)
            Konata.fontManager.getFont(size).drawStringWithShadow(text, x, y, color);
        else
            Konata.fontManager.getFont(size).drawString(text, x, y, color);
    }

    public static void drawCenteredString(int size, String text, float x, float y, int color) {
        assert Konata.fontManager != null;
        Konata.fontManager.getFont(size).drawCenteredString(text, x, y, color);
    }
}
