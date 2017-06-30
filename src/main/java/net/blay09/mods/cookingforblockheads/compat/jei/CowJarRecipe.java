package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CowJarRecipe implements IRecipeWrapper {

	private List<ItemStack> input = Lists.newArrayList(new ItemStack(Blocks.ANVIL), new ItemStack(ModBlocks.milkJar));
	private final ItemStack output = new ItemStack(ModBlocks.cowJar);

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, input);
		ingredients.setOutput(ItemStack.class, output);
	}

}
