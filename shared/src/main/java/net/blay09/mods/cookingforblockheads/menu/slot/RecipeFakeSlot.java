package net.blay09.mods.cookingforblockheads.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RecipeFakeSlot extends FakeSlot {

    private FoodRecipeWithStatus recipe;

    public RecipeFakeSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    @Override
    public ItemStack getItem() {
        return recipe != null ? recipe.getOutputItem() : ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return recipe != null;
    }

    @Override
    public boolean isActive() {
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
