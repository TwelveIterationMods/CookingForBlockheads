package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;

public interface IngredientPredicate {
    boolean test(ItemStack itemStack, int count);
}
