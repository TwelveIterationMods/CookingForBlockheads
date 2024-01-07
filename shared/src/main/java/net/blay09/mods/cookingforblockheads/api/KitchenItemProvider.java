package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;

public interface KitchenItemProvider {
    IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens);

    IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens);
}
