package cn.cutemic.konata.modules.plugin.api;

import cn.cutemic.konata.event.EventDispatcher;
import cn.cutemic.konata.modules.logger.Logger;

public class Plugin {
    public void init() {
        Logger.info("plugins", "Plugin initialized: " + this.getClass().getName());
        EventDispatcher.registerListener(this);
    }

    public boolean onJoin(String serverId) {
        return false;
    }
}
