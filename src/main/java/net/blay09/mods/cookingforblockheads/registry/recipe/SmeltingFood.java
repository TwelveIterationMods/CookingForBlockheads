package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.ItemStack;

public class SmeltingFood extends FoodRecipe {

    public SmeltingFood(int id, ItemStack outputItem, ItemStack sourceStack) {
        super(id);
        this.outputItem = outputItem;
        this.craftMatrix = Lists.newArrayList();
        this.craftMatrix.add(new FoodIngredient(sourceStack, false));
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SMELTING;
    }

}
