package net.blay09.mods.cookingforblockheads.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CowJarRecipeHandler implements IRecipeHandler<CowJarRecipe> {
	@Nonnull
	@Override
	public Class<CowJarRecipe> getRecipeClass() {
		return CowJarRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return CowJarRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CowJarRecipe recipe) {
		return CowJarRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull CowJarRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull CowJarRecipe recipe) {
		return true;
	}
}
