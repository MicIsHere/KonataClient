package cn.cutemic.konata.modules.plugin.api.client;

import cn.cutemic.konata.Konata;
import cn.cutemic.konata.ui.common.TextField;

public class PluginTextField {
    TextField textField;

    public PluginTextField(int size, String placeHolder, int color, int fontColor, int maxLength) {
        assert Konata.fontManager != null;
        textField = new TextField(Konata.fontManager.getFont(size), placeHolder, color, fontColor, maxLength);
    }

    public PluginTextField(int size, boolean hideContent, String placeHolder, int color, int fontColor, int maxLength) {
        assert Konata.fontManager != null;
        textField = new TextField(Konata.fontManager.getFont(size), hideContent, placeHolder, color, fontColor, maxLength);
    }

    public void drawTextBox(float x, float y, float width, float height) {
        textField.drawTextBox(x, y, width, height);
    }

    public String getContent() {
        return textField.text;
    }

    public void setContent(String text) {
        textField.text = text;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void textboxKeyTyped(char typedChar, int keyCode) {
        textField.textboxKeyTyped(typedChar, keyCode);
    }
}
