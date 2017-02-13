package net.blay09.mods.cookingforblockheads.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CowJarRecipe extends BlankRecipeWrapper {

	private final ItemStack input = new ItemStack(Blocks.ANVIL);
	private final ItemStack output = new ItemStack(ModBlocks.milkJar);

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutput(ItemStack.class, output);
	}

}
