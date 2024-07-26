package cn.cutemic.konata.modules.plugin.api.client;

import org.jetbrains.annotations.NotNull;
import cn.cutemic.konata.ui.common.GuiButton;

public class PluginButton extends GuiButton {
    public PluginButton(@NotNull String text, @NotNull Runnable runnable) {
        super(text, runnable);
    }

    public void draw(float x, float y, float width, float height, float mouseX, float mouseY) {
        super.render(x, y, width, height, mouseX, mouseY);
    }

    public void mouseClicked(float mouseX, float mouseY, int btn) {
        super.mouseClick(mouseX, mouseY, btn);
    }
}
