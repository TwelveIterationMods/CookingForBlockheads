package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.container.FoodRecipeWithStatus;
import net.minecraft.item.ItemStack;

public class FakeSlotRecipe extends FakeSlot {

	private FoodRecipeWithStatus recipe;

	public FakeSlotRecipe(int slotId, int x, int y) {
		super(slotId, x, y);
	}

	@Override
	public ItemStack getStack() {
		return recipe != null ? recipe.getOutputItem() : null;
	}

	@Override
	public boolean getHasStack() {
		return recipe != null;
	}

	@Override
	public boolean canBeHovered() {
		return recipe != null;
	}

	public void setFoodRecipe(FoodRecipeWithStatus recipe) {
		this.recipe = recipe;
	}

	public FoodRecipeWithStatus getRecipe() {
		return recipe;
	}
}
