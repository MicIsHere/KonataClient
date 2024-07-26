package cn.cutemic.konata.wrapper;

import org.jetbrains.annotations.NotNull;
import cn.cutemic.konata.interfaces.client.IConstantsProvider;

public class Constants implements IConstantsProvider {
    public static final String VERSION = "1.8.9";
    public static final String EDITION = "Forge";

    @NotNull
    @Override
    public String getVersion() {
        return VERSION;
    }

    @NotNull
    @Override
    public String getEdition() {
        return EDITION;
    }
}
