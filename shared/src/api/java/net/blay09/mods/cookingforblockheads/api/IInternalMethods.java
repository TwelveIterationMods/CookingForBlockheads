package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInternalMethods {
    void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler);

    void addOvenFuel(ItemStack fuelItem, int fuelTime);

    void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem);

    void addToolItem(ItemStack toolItem);

    void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler);

    void addWaterItem(ItemStack waterItem);

    void addMilkItem(ItemStack milkItem);

    void addCowClass(Class<? extends LivingEntity> clazz);

    IKitchenMultiBlock getKitchenMultiBlock(World world, BlockPos pos);

    void addSortButton(ISortButton button);
}
