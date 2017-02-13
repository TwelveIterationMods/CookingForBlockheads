package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FakeSlotRecipe extends FakeSlot {

	private FoodRecipeWithStatus recipe;

	public FakeSlotRecipe(int slotId, int x, int y) {
		super(slotId, x, y);
	}

	@Override
	public ItemStack getStack() {
		return recipe != null ? recipe.getOutputItem() : ItemStack.EMPTY;
	}

	@Override
	public boolean getHasStack() {
		return recipe != null;
	}

	@Override
	public boolean canBeHovered() {
		return recipe != null;
	}

	public void setFoodRecipe(@Nullable FoodRecipeWithStatus recipe) {
		this.recipe = recipe;
	}

	@Nullable
	public FoodRecipeWithStatus getRecipe() {
		return recipe;
	}

}
