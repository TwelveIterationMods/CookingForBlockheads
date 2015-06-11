package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.food.IFoodIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPreview extends Slot {

    private static final int ITEM_SWITCH_TIME = 20;

    private IFoodIngredient ingredient;
    private ItemStack[] visibleStacks;
    private IInventory sourceInventory;
    private boolean enabled = true;

    private int visibleItemTime;
    private int visibleItemIndex;

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
        if(ingredient != null) {
            visibleStacks = ingredient.getItemStacks();
            visibleItemTime = ITEM_SWITCH_TIME;
            visibleItemIndex = 0;
        } else {
            visibleStacks = null;
            putStack(null);
        }
    }

    public void update() {
        if(visibleStacks != null) {
            visibleItemTime++;
            if(visibleItemTime >= ITEM_SWITCH_TIME) {
                visibleItemIndex++;
                if(visibleItemIndex >= visibleStacks.length) {
                    visibleItemIndex = 0;
                }
                putStack(visibleStacks[visibleItemIndex]);
                visibleItemTime = 0;
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean canBeHovered() {
        return enabled;
    }

    public void setSourceInventory(IInventory sourceInventory) {
        this.sourceInventory = sourceInventory;
    }
}
