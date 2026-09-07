package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.mixin.ShapelessRecipeAccessor;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class KitchenShapelessRecipeHandler extends AbstractKitchenCraftingRecipeHandler<ShapelessRecipe> {
    @Override
    public int mapToMatrixSlot(ShapelessRecipe recipe, int ingredientIndex) {
        return ingredientIndex;
    }

    @Override
    public List<Optional<Ingredient>> getIngredients(ShapelessRecipe recipe) {
        return recipe instanceof ShapelessRecipeAccessor accessor ? accessor.getIngredients().stream().map(Optional::of).toList() : List.of();
    }

    @Override
    public @Nullable ItemStackTemplate predictResultItem(ShapelessRecipe recipe) {
        return ((ShapelessRecipeAccessor) recipe).getResult();
    }
}
