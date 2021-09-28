package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.item.ItemStack;

public class FoodRecipeWithStatus {
    private final ItemStack outputItem;
    private final RecipeStatus status;

    public FoodRecipeWithStatus(ItemStack outputItem, RecipeStatus status) {
        this.outputItem = outputItem;
        this.status = status;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public RecipeStatus getStatus() {
        return status;
    }
}
