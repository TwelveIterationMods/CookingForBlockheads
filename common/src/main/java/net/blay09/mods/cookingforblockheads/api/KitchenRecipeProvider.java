package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.resources.Identifier;

import java.util.Set;

/**
 * Recipe providers expose simple no-ingredient recipes that should be available in the kitchen.
 */
public interface KitchenRecipeProvider {
    Set<Identifier> getKitchenRecipeSources();
}
