package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface InternalMethods {

    void addOvenFuel(ItemStack fuelItem, int fuelTime);

    void addSortButton(ISortButton button);

    void setFoodStatsProvider(FoodStatsProvider foodStatsProvider);

    FoodStatsProvider getFoodStatsProvider();

    <C extends RecipeInput, T extends Recipe<C>> void registerKitchenRecipeHandler(Class<? extends T> recipeClass, KitchenRecipeHandler<C, ? extends T> kitchenRecipeHandler);

    <C extends RecipeInput, T extends Recipe<C>> KitchenRecipeHandler<C, T> getKitchenRecipeHandler(T recipe);
}
