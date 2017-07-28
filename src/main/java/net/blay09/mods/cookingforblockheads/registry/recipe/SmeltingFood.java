package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SmeltingFood extends FoodRecipe {

    public SmeltingFood(ItemStack outputItem, ItemStack sourceStack) {
        this.outputItem = outputItem;
        this.craftMatrix = Lists.newArrayList();
        this.craftMatrix.add(new FoodIngredient(Ingredient.fromStacks(sourceStack), false));
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SMELTING;
    }

}
