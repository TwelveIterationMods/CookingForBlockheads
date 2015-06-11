package net.blay09.mods.cookingbook.food.ingredient;

import net.minecraft.item.ItemStack;

import java.util.List;

public class OreDictToolIngredient extends OreDictIngredient {

    public OreDictToolIngredient(List<ItemStack> validItemList) {
        super(validItemList);
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}
