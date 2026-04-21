package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.config.ConfigLocalization;

public class CookingForBlockheadsConfig {
    public static CookingForBlockheadsConfigData getActive() {
        return Balm.getConfig().getActive(CookingForBlockheadsConfigData.class);
    }

    public static void initialize() {
        ConfigLocalization.enableModernTranslationKeys(CookingForBlockheads.MOD_ID);
        Balm.getConfig().registerConfig(CookingForBlockheadsConfigData.class, null);
    }
}
