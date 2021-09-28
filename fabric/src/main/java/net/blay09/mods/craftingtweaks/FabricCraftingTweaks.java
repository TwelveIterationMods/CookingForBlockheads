package net.blay09.mods.craftingtweaks;

import net.fabricmc.api.ModInitializer;

public class FabricCraftingTweaks implements ModInitializer {
    @Override
    public void onInitialize() {
        CraftingTweaks.initialize();
    }
}
