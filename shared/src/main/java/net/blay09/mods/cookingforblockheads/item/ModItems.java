package net.blay09.mods.cookingforblockheads.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static Item recipeBook;
    public static Item noFilterBook;
    public static Item craftingBook;
    public static Item heatingUnit;
    public static Item iceUnit;
    public static Item preservationChamber;

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                noFilterBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.NOFILTER).setRegistryName("no_filter_edition"),
                recipeBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.RECIPE).setRegistryName("recipe_book"),
                craftingBook = new ItemRecipeBook(ItemRecipeBook.RecipeBookEdition.CRAFTING).setRegistryName("crafting_book"),
                heatingUnit = new ItemHeatingUnit().setRegistryName(ItemHeatingUnit.name),
                iceUnit = new ItemIceUnit().setRegistryName(ItemIceUnit.name),
                preservationChamber = new ItemPreservationChamber().setRegistryName(ItemPreservationChamber.name)
        );
    }

}
