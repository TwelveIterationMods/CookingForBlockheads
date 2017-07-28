package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FoodIngredient {

    private final Ingredient ingredient;
    private final boolean isToolItem;

    public FoodIngredient(Ingredient ingredient, boolean isToolItem) {
        this.ingredient = ingredient;
        this.isToolItem = isToolItem;
    }

    public boolean isValidItem(ItemStack itemStack) {
        return ingredient.apply(itemStack);
    }

    public ItemStack[] getItemStacks() {
        return ingredient.getMatchingStacks();
    }

    public boolean isToolItem() {
        return isToolItem;
    }

}
