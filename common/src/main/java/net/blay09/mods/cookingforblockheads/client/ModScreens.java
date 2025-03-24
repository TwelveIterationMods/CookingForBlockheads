package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.cookingforblockheads.client.gui.screen.*;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(id("spice_rack"), ModMenus.spiceRack::get, SpiceRackScreen::new);
        screens.registerScreen(id("oven"), ModMenus.oven::get, OvenScreen::new);
        screens.registerScreen(id("counter"), ModMenus.counter::get, CounterScreen::new);
        screens.registerScreen(id("fridge"), ModMenus.fridge::get, FridgeScreen::new);
        screens.registerScreen(id("fruit_basket"), ModMenus.fruitBasket::get, FruitBasketScreen::new);
        screens.registerScreen(id("no_filter_book"), ModMenus.noFilterBook::get, KitchenScreen::new);
        screens.registerScreen(id("recipe_book"), ModMenus.recipeBook::get, KitchenScreen::new);
        screens.registerScreen(id("crafting_book"), ModMenus.craftingBook::get, KitchenScreen::new);
        screens.registerScreen(id("cooking_table"), ModMenus.cookingTable::get, KitchenScreen::new);
        screens.registerScreen(id("cutting_board"), ModMenus.cuttingBoard::get, CuttingBoardScreen::new);
    }
}
