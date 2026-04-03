package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface Kitchen {
    CraftingContext createCraftingContext(@Nullable Player player);
    List<KitchenRecipeProvider> getRecipeProviders();
    boolean canProcess(RecipeType<?> recipeType);
}
