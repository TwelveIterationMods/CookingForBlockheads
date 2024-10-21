package net.blay09.mods.cookingforblockheads.recipe;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {

    public static final String TOASTER_RECIPE_GROUP = "toaster";
    public static final String OVEN_RECIPE_GROUP = "oven";
    public static final ResourceLocation TOASTER_RECIPE_ID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, TOASTER_RECIPE_GROUP);
    public static final ResourceLocation OVEN_RECIPE_ID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, OVEN_RECIPE_GROUP);

    public static RecipeBookCategory toasterRecipeBookCategory;
    public static RecipeType<ToasterRecipe> toasterRecipeType;
    public static RecipeSerializer<ToasterRecipe> toasterRecipeSerializer;

    public static RecipeBookCategory ovenRecipeBookCategory;
    public static RecipeType<OvenRecipe> ovenRecipeType;
    public static RecipeSerializer<OvenRecipe> ovenRecipeSerializer;

    public static void initialize(BalmRecipes registry) {
        registry.registerRecipeType(() -> toasterRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return TOASTER_RECIPE_GROUP;
                    }
                },
                () -> toasterRecipeSerializer = new ToasterRecipe.Serializer(), TOASTER_RECIPE_ID);
        registry.registerRecipeType(() -> ovenRecipeType = new RecipeType<>() {
                    @Override
                    public String toString() {
                        return OVEN_RECIPE_GROUP;
                    }
                },
                () -> ovenRecipeSerializer = new OvenRecipe.Serializer(), OVEN_RECIPE_ID);
    }
}
