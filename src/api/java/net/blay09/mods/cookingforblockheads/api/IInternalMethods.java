package net.blay09.mods.cookingforblockheads.api;

import java.util.List;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInternalMethods {

    void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler);
    void addOvenFuel(ItemStack fuelItem, int fuelTime);
    void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem);
    void addToolItem(ItemStack toolItem);
    void addToastHandler(ItemStack itemStack, ToastHandler toastHandler);
    void addWaterItem(ItemStack waterItem);
    void addMilkItem(ItemStack milkItem);
    void addCowClass(Class<? extends EntityLivingBase> clazz);
	List<IKitchenItemProvider> getItemProviders(World world, BlockPos pos, InventoryPlayer player);

}
