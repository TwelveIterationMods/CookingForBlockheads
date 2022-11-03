package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.minecraft.world.item.ItemStack;

public interface IngredientPredicateWithCache extends IngredientPredicate {
    ItemStack[] getItems();
}
