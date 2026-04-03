package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.blay09.mods.cookingforblockheads.crafting.CraftingOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface Kitchen {
    CraftingContext createCraftingContext(@Nullable Player player);
    boolean canProcess(RecipeType<?> recipeType);

    @Deprecated
    boolean isRecipeAvailable(CraftingOperation operation);

    Collection<RecipeHolder<?>> getRecipesFor(ItemStack resultItem);

    Collection<RecipeHolder<?>> getAvailableRecipes();
}
