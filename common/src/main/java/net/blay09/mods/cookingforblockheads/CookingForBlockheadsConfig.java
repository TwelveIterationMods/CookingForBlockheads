package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;

public class CookingForBlockheadsConfig {
    public static CookingForBlockheadsConfigData getActive() {
        return Balm.getConfig().getActive(CookingForBlockheadsConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(CookingForBlockheadsConfigData.class, null);
    }
}
