package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.client.gui.screen.*;
import net.blay09.mods.cookingforblockheads.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.registerFactory(ModContainers.spiceRack, SpiceRackScreen::new);
        ScreenManager.registerFactory(ModContainers.oven, OvenScreen::new);
        ScreenManager.registerFactory(ModContainers.counter, CounterScreen::new);
        ScreenManager.registerFactory(ModContainers.fridge, FridgeScreen::new);
        ScreenManager.registerFactory(ModContainers.fruitBasket, FruitBasketScreen::new);
        ScreenManager.registerFactory(ModContainers.noFilterBook, RecipeBookScreen::new);
        ScreenManager.registerFactory(ModContainers.recipeBook, RecipeBookScreen::new);
        ScreenManager.registerFactory(ModContainers.craftingBook, RecipeBookScreen::new);
        ScreenManager.registerFactory(ModContainers.cookingTable, RecipeBookScreen::new);
    }
}
