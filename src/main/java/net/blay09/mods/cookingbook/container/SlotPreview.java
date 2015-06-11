package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPreview extends Slot {

    private IFoodIngredient ingredient;
    private boolean enabled = true;

    public SlotPreview(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    public void setIngredient(IFoodIngredient ingredient) {
        this.ingredient = ingredient;
        putStack(ingredient != null ? ingredient.getItemStack() : null);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean canBeHovered() {
        return enabled;
    }

}
