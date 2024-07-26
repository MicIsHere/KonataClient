package cn.cutemic.konata.wrapper;

import net.minecraft.client.settings.KeyBinding;
import cn.cutemic.konata.forge.api.IKeyBinding;
import cn.cutemic.konata.interfaces.game.IGameSettings;

public class GameSettingsProvider implements IGameSettings {
    public void setKeyPress(KeyBinding key, boolean value){
        ((IKeyBinding) key).setPressed(value);
    }
}
