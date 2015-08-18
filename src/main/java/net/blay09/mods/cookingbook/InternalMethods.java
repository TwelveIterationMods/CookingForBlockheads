package net.blay09.mods.cookingbook;

import net.blay09.mods.cookingbook.api.IInternalMethods;
import net.minecraft.item.ItemStack;

public class InternalMethods implements IInternalMethods {

    @Override
    public void addSinkRecipe(ItemStack itemStackIn, ItemStack itemStackOut) {
        SinkRecipes.addSinkRecipe(itemStackIn, itemStackOut);
    }

}
