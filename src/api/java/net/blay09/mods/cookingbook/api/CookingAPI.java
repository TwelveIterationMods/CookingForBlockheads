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

}
