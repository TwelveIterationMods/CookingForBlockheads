package net.blay09.mods.cookingbook.api;

import net.minecraft.item.ItemStack;

public class CookingAPI {

    private static IInternalMethods internalMethods;

    public static void setupAPI(IInternalMethods internalMethods) {
        CookingAPI.internalMethods = internalMethods;
    }

    public static void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        internalMethods.addSinkHandler(itemStack, sinkHandler);
    }

    public static void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        internalMethods.addOvenFuel(fuelItem, fuelTime);
    }

    public static void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem) {
        internalMethods.addOvenRecipe(sourceItem, resultItem);
    }

    public static void addToolItem(ItemStack toolItem) {
        internalMethods.addToolItem(toolItem);
    }

}
