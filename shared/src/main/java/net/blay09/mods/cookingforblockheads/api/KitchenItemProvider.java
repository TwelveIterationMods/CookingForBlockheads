package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collection;

/**
 * Item Providers are responsible for locating and supplying ingredients to recipes.
 * <p>
 * If your block is a simple storage container providing either a Container or ItemHandler,
 * you can tag it <code>cookingforblockheads:kitchen_item_providers</code> to have it automatically
 * use the default implementation.
 * <p>
 * This interface is specifically for cases where you want to provide custom logic for locating ingredients.
 */
public interface KitchenItemProvider {
    /**
     * @param ingredient the ingredient to find
     * @param ingredientTokens the ingredient tokens that have already been provided by this provider
     * @return an ingredient token that matches the given ingredient, or null if none was found
     */
    IngredientToken findIngredient(Ingredient ingredient, Collection<IngredientToken> ingredientTokens);

    /**
     * @param itemStack the item to find
     * @param ingredientTokens the ingredient tokens that have already been provided by this provider
     * @return an ingredient token that matches the given ingredient, or null if none was found
     */
    IngredientToken findIngredient(ItemStack itemStack, Collection<IngredientToken> ingredientTokens);
}
