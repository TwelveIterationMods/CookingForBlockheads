package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.cookingforblockheads.client.gui.screen.*;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;

public class ModMenuScreens {
    public static void initialize(BalmMenuScreenRegistrar screens) {
        screens.register(ModMenus.spiceRack, SpiceRackScreen::new);
        screens.register(ModMenus.oven, OvenScreen::new);
        screens.register(ModMenus.counter, CounterScreen::new);
        screens.register(ModMenus.fridge, FridgeScreen::new);
        screens.register(ModMenus.fruitBasket, FruitBasketScreen::new);
        screens.register(ModMenus.noFilterBook, KitchenScreen::new);
        screens.register(ModMenus.recipeBook, KitchenScreen::new);
        screens.register(ModMenus.craftingBook, KitchenScreen::new);
        screens.register(ModMenus.cookingTable, KitchenScreen::new);
        screens.register(ModMenus.cuttingBoard, CuttingBoardScreen::new);
    }
}
