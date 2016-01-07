package net.blay09.mods.cookingbook;

import net.blay09.mods.cookingbook.api.IInternalMethods;
import net.blay09.mods.cookingbook.api.SinkHandler;
import net.blay09.mods.cookingbook.api.ToastHandler;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.minecraft.item.ItemStack;

public class InternalMethods implements IInternalMethods {

    @Override
    public void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        CookingRegistry.addSinkHandler(itemStack, sinkHandler);
    }

    @Override
    public void addToastHandler(ItemStack itemStack, ToastHandler toastHandler) {
        CookingRegistry.addToastHandler(itemStack, toastHandler);
    }

    @Override
    public void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        CookingRegistry.addOvenFuel(fuelItem, fuelTime);
    }

    @Override
    public void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem) {
        CookingRegistry.addSmeltingItem(sourceItem, resultItem);
    }

    @Override
    public void addToolItem(ItemStack toolItem) {
        CookingRegistry.addToolItem(toolItem);
    }

}
