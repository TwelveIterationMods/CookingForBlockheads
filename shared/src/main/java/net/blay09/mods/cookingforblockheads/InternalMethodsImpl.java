package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.*;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class InternalMethodsImpl implements InternalMethods {

    private FoodStatsProvider foodStatsProvider;

    @Override
    public void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        CookingForBlockheadsRegistry.addOvenFuel(fuelItem, fuelTime);
    }

    @Override
    public Kitchen createKitchen(Level level, BlockPos pos) {
        return KitchenMultiBlock.buildFromLocation(level, pos);
    }

    @Override
    public Kitchen createKitchen(ItemStack itemStack) {
        return KitchenMultiBlock.buildFromItemStack(itemStack);
    }

    @Override
    public void addSortButton(ISortButton button) {
        CookingForBlockheadsRegistry.addSortButton(button);
    }

    @Override
    public void setFoodStatsProvider(FoodStatsProvider foodStatsProvider) {
        this.foodStatsProvider = foodStatsProvider;
    }

    @Override
    public FoodStatsProvider getFoodStatsProvider() {
        return foodStatsProvider;
    }

    @Override
    public <T extends Recipe<?>> void registerKitchenRecipeHandler(Class<T> recipeClass, KitchenRecipeHandler<T> kitchenRecipeHandler) {
        // TODO
    }
}
