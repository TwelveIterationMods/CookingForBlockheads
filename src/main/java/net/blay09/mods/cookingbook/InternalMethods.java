package net.blay09.mods.cookingbook;

import net.blay09.mods.cookingbook.api.IInternalMethods;
import net.blay09.mods.cookingbook.api.SinkHandler;
import net.minecraft.item.ItemStack;

public class InternalMethods implements IInternalMethods {

    @Override
    public void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        SinkHandlers.addSinkHandler(itemStack, sinkHandler);
    }

    @Override
    public void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        OvenFuels.addOvenFuel(fuelItem, fuelTime);
    }

    @Override
    public void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem) {
        OvenRecipes.addSmeltingItem(sourceItem, resultItem);
    }

}
