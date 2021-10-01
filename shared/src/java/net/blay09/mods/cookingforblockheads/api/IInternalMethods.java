package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IInternalMethods {
    void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler);

    void addOvenFuel(ItemStack fuelItem, int fuelTime);

    void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem);

    void addToolItem(ItemStack toolItem);

    void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler);

    void addWaterItem(ItemStack waterItem);

    void addMilkItem(ItemStack milkItem);

    void addCowClass(Class<? extends LivingEntity> clazz);

    IKitchenMultiBlock getKitchenMultiBlock(Level level, BlockPos pos);

    void addSortButton(ISortButton button);
}
