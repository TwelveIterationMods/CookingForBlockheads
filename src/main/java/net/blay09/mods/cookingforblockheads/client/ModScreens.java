package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.client.gui.screen.SpiceRackScreen;
import net.blay09.mods.cookingforblockheads.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.registerFactory(ModContainers.spiceRack, SpiceRackScreen::new);
    }
}
