package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.cookingforblockheads.client.gui.screen.*;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(id("spice_rack"), ModMenus.spiceRack::value, SpiceRackScreen::new);
        screens.registerScreen(id("oven"), ModMenus.oven::value, OvenScreen::new);
        screens.registerScreen(id("counter"), ModMenus.counter::value, CounterScreen::new);
        screens.registerScreen(id("fridge"), ModMenus.fridge::value, FridgeScreen::new);
        screens.registerScreen(id("fruit_basket"), ModMenus.fruitBasket::value, FruitBasketScreen::new);
        screens.registerScreen(id("no_filter_book"), ModMenus.noFilterBook::value, KitchenScreen::new);
        screens.registerScreen(id("recipe_book"), ModMenus.recipeBook::value, KitchenScreen::new);
        screens.registerScreen(id("crafting_book"), ModMenus.craftingBook::value, KitchenScreen::new);
        screens.registerScreen(id("cooking_table"), ModMenus.cookingTable::value, KitchenScreen::new);
        screens.registerScreen(id("cutting_board"), ModMenus.cuttingBoard::value, CuttingBoardScreen::new);
    }
}
