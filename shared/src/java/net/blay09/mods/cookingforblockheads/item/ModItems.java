package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static final CreativeModeTab creativeModeTab = Balm.getItems().createCreativeModeTab(id("cookingforblockheads"), () -> new ItemStack(ModItems.recipeBook));

    public static Item recipeBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.RECIPE);
    public static Item noFilterBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.NO_FILTER);
    public static Item craftingBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.CRAFTING);
    public static Item heatingUnit = new ItemHeatingUnit();
    public static Item iceUnit = new ItemIceUnit();
    public static Item preservationChamber = new ItemPreservationChamber();

    public static void initialize(BalmItems items) {
            items.registerItem(() -> noFilterBook, id("no_filter_edition"));
            items.registerItem(() -> recipeBook, id("recipe_book"));
            items.registerItem(() -> craftingBook, id("crafting_book"));
            items.registerItem(() -> heatingUnit, id(ItemHeatingUnit.name));
            items.registerItem(() -> iceUnit, id(ItemIceUnit.name));
            items.registerItem(() -> preservationChamber, id(ItemPreservationChamber.name));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }
}
