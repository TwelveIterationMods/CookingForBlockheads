package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public interface InternalMethods {

    void addOvenFuel(ItemStack fuelItem, int fuelTime);

    Kitchen createKitchen(Level level, BlockPos pos);

    Kitchen createKitchen(ItemStack itemStack);

    void addSortButton(ISortButton button);

    void setFoodStatsProvider(FoodStatsProvider foodStatsProvider);

    FoodStatsProvider getFoodStatsProvider();

    <T extends Recipe<?>> void registerKitchenRecipeHandler(Class<T> recipeClass, KitchenRecipeHandler<T> kitchenRecipeHandler);
    
}
