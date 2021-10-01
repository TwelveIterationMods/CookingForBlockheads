package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CookingForBlockheadsAPI {

    private static IInternalMethods internalMethods;
    private static FoodStatsProvider foodStatsProvider;

    public static void setupAPI(IInternalMethods internalMethods) {
        CookingForBlockheadsAPI.internalMethods = internalMethods;
    }

    public static void setFoodStatsProvider(FoodStatsProvider foodStatsProvider) {
        CookingForBlockheadsAPI.foodStatsProvider = foodStatsProvider;
    }

    public static FoodStatsProvider getFoodStatsProvider() {
        return foodStatsProvider;
    }

    public static void addSinkHandler(ItemStack itemStack, SinkHandler sinkHandler) {
        internalMethods.addSinkHandler(itemStack, sinkHandler);
    }

    public static void addToasterHandler(ItemStack itemStack, ToasterHandler toastHandler) {
        internalMethods.addToasterHandler(itemStack, toastHandler);
    }

    public static void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        internalMethods.addOvenFuel(fuelItem, fuelTime);
    }

    public static void addOvenRecipe(ItemStack sourceItem, ItemStack resultItem) {
        internalMethods.addOvenRecipe(sourceItem, resultItem);
    }

    public static void addToolItem(ItemStack toolItem) {
        internalMethods.addToolItem(toolItem);
    }

    public static void addWaterItem(ItemStack waterItem) {
        internalMethods.addWaterItem(waterItem);
    }

    public static void addMilkItem(ItemStack milkItem) {
        internalMethods.addMilkItem(milkItem);
    }

    public static void addCowClass(Class<? extends LivingEntity> clazz) {
        internalMethods.addCowClass(clazz);
    }

    public static IKitchenMultiBlock getKitchenMultiBlock(Level level, BlockPos pos) {
        return internalMethods.getKitchenMultiBlock(level, pos);
    }

    public static void addSortButton(ISortButton button) {
        internalMethods.addSortButton(button);
    }

}
