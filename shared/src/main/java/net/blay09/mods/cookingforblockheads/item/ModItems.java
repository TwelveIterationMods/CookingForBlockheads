package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static DeferredObject<CreativeModeTab> creativeModeTab;

    public static Item recipeBook;
    public static Item noFilterBook;
    public static Item craftingBook;
    public static Item heatingUnit;
    public static Item iceUnit;
    public static Item preservationChamber;

    public static void initialize(BalmItems items) {
        items.registerItem(() -> recipeBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.RECIPE), id("recipe_book"));
        items.registerItem(() -> noFilterBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.NO_FILTER), id("no_filter_edition"));
        items.registerItem(() -> craftingBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.CRAFTING), id("crafting_book"));
        items.registerItem(() -> heatingUnit = new ItemHeatingUnit(), id("heating_unit"));
        items.registerItem(() -> iceUnit = new ItemIceUnit(), id("ice_unit"));
        items.registerItem(() -> preservationChamber = new ItemPreservationChamber(), id("preservation_chamber"));

        creativeModeTab = items.registerCreativeModeTab(id("cookingforblockheads"), () -> new ItemStack(ModItems.recipeBook));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }
}
