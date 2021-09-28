package net.blay09.mods.craftingtweaks.client;

import net.fabricmc.api.ClientModInitializer;

public class FabricCraftingTweaksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CraftingTweaksClient.initialize();
    }
}
