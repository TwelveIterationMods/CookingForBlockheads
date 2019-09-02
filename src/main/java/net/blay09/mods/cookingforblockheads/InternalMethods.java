package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InternalMethods implements IInternalMethods {

    @Override
    public void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        CookingRegistry.addSinkHandler(itemStack, sinkHandler);
    }

    @Override
    public void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler) {
        CookingRegistry.addToasterHandler(itemStack, toastHandler);
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
    public void addCowClass(Class<? extends LivingEntity> clazz) {
        CowJarHandler.registerCowClass(clazz);
    }

    @Override
    public IKitchenMultiBlock getKitchenMultiBlock(World world, BlockPos pos) {
        return new KitchenMultiBlock(world, pos);
    }

    @Override
    public void addSortButton(ISortButton button) {
        CookingRegistry.addSortButton(button);
    }
}
