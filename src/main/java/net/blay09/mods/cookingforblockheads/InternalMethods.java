package net.blay09.mods.cookingforblockheads;

import java.util.List;

import net.blay09.mods.cookingforblockheads.api.ICustomSortButton;
import net.blay09.mods.cookingforblockheads.api.IInternalMethods;
import net.blay09.mods.cookingforblockheads.api.SinkHandler;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void addWaterItem(ItemStack waterItem) {
        CookingRegistry.addWaterItem(waterItem);
    }

    @Override
    public void addMilkItem(ItemStack milkItem) {
        CookingRegistry.addMilkItem(milkItem);
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

    @Override
    public void addCowClass(Class<? extends EntityLivingBase> clazz) {
        CookingForBlockheads.instance.cowJarHandler.registerCowClass(clazz);
    }
    
    @Override
    public List<IKitchenItemProvider> getItemProviders(World world, BlockPos pos, InventoryPlayer player) {
        return CookingRegistry.getItemProviders(new KitchenMultiBlock(world, pos), player);
    }

	@Override
	public void addCustomSortButton(ICustomSortButton button) {
		CookingRegistry.addCustomSortButton(button);
	}
}
