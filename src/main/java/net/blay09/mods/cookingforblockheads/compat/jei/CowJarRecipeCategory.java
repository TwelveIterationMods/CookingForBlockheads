package net.blay09.mods.cookingforblockheads.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CowJarRecipeCategory extends BlankRecipeCategory<CowJarRecipe> {

	private static final ResourceLocation texture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/jeiCowJar.png");

	public static final String UID = "cookingforblockheads.cowjar";
	private final IDrawableStatic background;
	private final String title;
	private final IDrawableStatic overlay;

	public CowJarRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(150, 110);
		this.title = I18n.format("jei.cookingforblockheads:cowJar");
		this.overlay = guiHelper.createDrawable(texture, 0, 0, 64, 80);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft, 56, 20);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CowJarRecipe recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 64, 0);
		recipeLayout.getItemStacks().init(1, false, 64, 76);
		recipeLayout.getItemStacks().set(ingredients);
	}

}
