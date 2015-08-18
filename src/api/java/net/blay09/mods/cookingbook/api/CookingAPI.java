package net.blay09.mods.cookingbook.api;

import net.minecraft.item.ItemStack;

public class CookingAPI {

    private static IInternalMethods internalMethods;

    public static void setupAPI(IInternalMethods internalMethods) {
        CookingAPI.internalMethods = internalMethods;
    }

    public static void addSinkRecipe(ItemStack itemStackIn, ItemStack itemStackOut) {
        internalMethods.addSinkRecipe(itemStackIn, itemStackOut);
    }

}
