package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface Kitchen {
    List<KitchenItemProvider> getItemProviders(@Nullable Player player);
    List<KitchenRecipeProvider> getRecipeProviders();
    List<KitchenItemProcessor> getItemProcessors();

    boolean canProcess(RecipeType<?> recipeType);
}
