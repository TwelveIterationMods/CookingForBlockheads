package net.blay09.mods.cookingforblockheads.recipe;

import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.item.crafting.DeferredRecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class ModRecipes {

    public static DeferredRecipeType<SingleRecipeInput, ToasterRecipe> toasterRecipes;
    public static DeferredRecipeType<SingleRecipeInput, OvenRecipe> ovenRecipes;

    public static void initialize(BalmRecipeTypeRegistrar recipeTypes) {
        toasterRecipes = recipeTypes.register("toaster", ToasterRecipe.class)
                .withSerializer(ToasterRecipe.Serializer::new)
                .withRecipeBookCategory()
                .asDeferredRecipeType();

        ovenRecipes = recipeTypes.register("oven", OvenRecipe.class)
                .withSerializer(OvenRecipe.Serializer::new)
                .withRecipeBookCategory()
                .asDeferredRecipeType();
    }
}
