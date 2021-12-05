package net.blay09.mods.cookingforblockheads.registry.recipe;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class FoodRecipe {

    protected List<FoodIngredient> craftMatrix;
    protected ItemStack outputItem;
    protected int recipeWidth = 3;

    public List<FoodIngredient> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FoodRecipeType getType() {
        return FoodRecipeType.CRAFTING;
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }

    @Nullable
    public ResourceLocation getRegistryName() {
        return Balm.getRegistries().getKey(outputItem.getItem());
    }

    public void fillCraftMatrix(Recipe<?> recipe) {
        craftMatrix = Lists.newArrayList();
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient != Ingredient.EMPTY) {
                boolean isToolItem = CookingRegistry.isToolItem(ingredient);
                craftMatrix.add(new FoodIngredient(ingredient, isToolItem));
            } else {
                craftMatrix.add(null);
            }
        }
    }
}
