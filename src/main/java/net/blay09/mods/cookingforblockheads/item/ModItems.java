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
                recipeBook = new ItemRecipeBook().setRegistryName(ItemRecipeBook.name),
                heatingUnit = new ItemHeatingUnit().setRegistryName(ItemHeatingUnit.name),
                iceUnit = new ItemIceUnit().setRegistryName(ItemIceUnit.name),
                preservationChamber = new ItemPreservationChamber().setRegistryName(ItemPreservationChamber.name)
        );
    }

}
