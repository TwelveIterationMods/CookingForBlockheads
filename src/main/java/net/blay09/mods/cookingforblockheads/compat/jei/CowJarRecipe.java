package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CowJarRecipe implements IRecipeWrapper {

	private List inputs = ImmutableList.of(new ItemStack(ModBlocks.milkJar));
	private List outputs = ImmutableList.of(new ItemStack(ModBlocks.cowJar));

	@Override
	public List getInputs() {
		return inputs;
	}

	@Override
	public List getOutputs() {
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return ImmutableList.of();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return ImmutableList.of();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return ImmutableList.of();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}
}
