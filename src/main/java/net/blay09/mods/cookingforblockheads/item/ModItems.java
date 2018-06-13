package net.blay09.mods.cookingforblockheads.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class ModItems {

    private static final List<Item> items = Lists.newArrayList();

    public static Item recipeBook;
    public static Item craftingBook;
    public static Item noFilterBook;
    public static Item heatingUnit;
    public static Item iceUnit;
    public static Item preservationChamber;

    public static void register(IForgeRegistry<Item> registry) {
        recipeBook = registerItem(registry, new ItemRecipeBook(ItemRecipeBook.BookType.RECIPE_BOOK));
        craftingBook = registerItem(registry, new ItemRecipeBook(ItemRecipeBook.BookType.CRAFTING_BOOK));
        noFilterBook = registerItem(registry, new ItemRecipeBook(ItemRecipeBook.BookType.NO_FILTER_BOOK));
        heatingUnit = registerItem(registry, new ItemHeatingUnit());
        iceUnit = registerItem(registry, new ItemIceUnit());
        preservationChamber = registerItem(registry, new ItemPreservationChamber());
    }

    private static Item registerItem(IForgeRegistry<Item> registry, Item item) {
        ResourceLocation registryName = ((IRegisterableItem) item).createRegistryName();
        item.setRegistryName(registryName);
        item.setUnlocalizedName(registryName.toString());
        registry.register(item);
        items.add(item);
        return item;
    }

    public static void registerModels() {
        for (Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(((IRegisterableItem) item).createRegistryName(), "inventory"));
        }
    }

}
