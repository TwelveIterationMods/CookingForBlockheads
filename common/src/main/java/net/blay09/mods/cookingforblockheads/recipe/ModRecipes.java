package net.blay09.mods.cookingforblockheads.recipe;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModRecipes {

    public static RecipeBookCategory toasterRecipeBookCategory;
    public static RecipeType<ToasterRecipe> toasterRecipeType;
    public static RecipeSerializer<ToasterRecipe> toasterRecipeSerializer;

    public static RecipeBookCategory ovenRecipeBookCategory;
    public static RecipeType<OvenRecipe> ovenRecipeType;
    public static RecipeSerializer<OvenRecipe> ovenRecipeSerializer;

    public static void initialize(BalmRecipes registry) {
        registry.registerRecipeType((identifier) -> toasterRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return identifier.getPath();
                    }
                }, id("toaster"));
        registry.registerRecipeSerializer(() -> toasterRecipeSerializer = new ToasterRecipe.Serializer(), id("toaster"));
        registry.registerRecipeType((identifier) -> ovenRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return identifier.getPath();
                    }
                }, id("oven"));
        registry.registerRecipeSerializer(() -> ovenRecipeSerializer = new OvenRecipe.Serializer(), id("oven"));
    }
}
