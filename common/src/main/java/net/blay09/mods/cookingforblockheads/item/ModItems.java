package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

        creativeModeTab = items.registerCreativeModeTab(() -> new ItemStack(ModBlocks.cowJar), id("cookingforblockheads"));
        items.setCreativeModeTabSorting(id("cookingforblockheads"), new Comparator<>() {
            private static final String[] patternStrings = new String[]{
                    "recipe_book",
                    "crafting_book",
                    "cooking_table",
                    "white_fridge",
                    "white_oven",
                    "sink",
                    "counter",
                    "cabinet",
                    "connector",
                    "white_kitchen_floor",
                    "milk_jar",
                    "cow_jar",
                    "toaster",
                    "tool_rack",
                    "spice_rack",
                    "fruit_basket",
                    "cutting_board",
                    "ice_unit",
                    "preservation_chamber",
                    "heating_unit",
                    "no_filter_edition",
                    ".+_cooking_table",
                    ".+_fridge",
                    ".+_oven",
                    ".+_sink",
                    ".+_counter",
                    ".+_cabinet",
                    ".+_kitchen_floor",
                    ".+_connector",
            };

            private static final Map<String, Integer> indexMap = new HashMap<>();
            private static final Map<Pattern, Integer> patternIndexMap = new HashMap<>();

            static {
                for (int i = 0; i < patternStrings.length; i++) {
                    final var patternString = patternStrings[i];
                    indexMap.put(patternString, i);
                    patternIndexMap.put(Pattern.compile(patternString), i);
                }
            }

            private static int getIndex(String name) {
                final var index = indexMap.get(name);
                if (index != null) {
                    return index;
                }

                for (var entry : patternIndexMap.entrySet()) {
                    if (entry.getKey().matcher(name).matches()) {
                        return entry.getValue();
                    }
                }

                return -1;
            }

            @Override
            public int compare(ItemLike o1, ItemLike o2) {
                final var id1 = BuiltInRegistries.ITEM.getKey(o1.asItem());
                final var id2 = BuiltInRegistries.ITEM.getKey(o2.asItem());
                final var name1 = id1.getPath();
                final var name2 = id2.getPath();
                final var index1 = getIndex(name1);
                final var index2 = getIndex(name2);
                if (index1 != -1 && index2 != -1) {
                    return Integer.compare(index1, index2);
                } else if (index1 != -1) {
                    return -1;
                } else if (index2 != -1) {
                    return 1;
                }

                return name1.compareTo(name2);
            }
        });
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, name);
    }
}
