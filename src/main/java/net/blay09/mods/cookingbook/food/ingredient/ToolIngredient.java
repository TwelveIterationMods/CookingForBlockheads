package net.blay09.mods.cookingbook.food.ingredient;

import net.minecraft.item.ItemStack;

public class ToolIngredient extends DefaultIngredient {

    public ToolIngredient(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}
