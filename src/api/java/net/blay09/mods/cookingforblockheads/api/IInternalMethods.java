package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IInternalMethods {

    void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler);
    void addOvenFuel(ItemStack fuelItem, int fuelTime);
    void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem);
    void addToolItem(ItemStack toolItem);
    void addToastHandler(ItemStack itemStack, ToastHandler toastHandler);
    void addWaterItem(ItemStack waterItem);
    void addMilkItem(ItemStack milkItem);
    void addCowClass(Class<? extends EntityLivingBase> clazz);
	void addCustomSortButton(ICustomSortButton button);

}
