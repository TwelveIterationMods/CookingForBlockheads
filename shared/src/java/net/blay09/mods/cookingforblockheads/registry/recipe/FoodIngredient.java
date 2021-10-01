package net.blay09.mods.cookingforblockheads.registry.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class FoodIngredient {

    private final Ingredient ingredient;
    private final boolean isToolItem;

    public FoodIngredient(Ingredient ingredient, boolean isToolItem) {
        this.ingredient = ingredient;
        this.isToolItem = isToolItem;
    }

    public boolean isValidItem(ItemStack itemStack) {
        return ingredient.test(itemStack);
    }

    public ItemStack[] getItemStacks() {
        return ingredient.getItems();
    }

    public boolean isToolItem() {
        return isToolItem;
    }

}
