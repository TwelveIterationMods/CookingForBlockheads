package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SlotPreview extends Slot {

    private static final int ITEM_SWITCH_TIME = 20;

    private FoodIngredient ingredient;
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

    public void setIngredient(FoodIngredient ingredient) {
        this.ingredient = ingredient;
        if(ingredient != null) {
            visibleStacks = ingredient.getItemStacks();
            if(ingredient.getItemStacks().length > 1 && !ingredient.isToolItem()) {
                List<ItemStack> visibleStackList = new ArrayList<ItemStack>();
                for(ItemStack visibleStack : visibleStacks) {
                    for(int i = 0; i < sourceInventory.getSizeInventory(); i++) {
                        ItemStack itemStack = sourceInventory.getStackInSlot(i);
                        if(itemStack != null) {
                            if(itemStack.getHasSubtypes() ? itemStack.isItemEqual(visibleStack) : itemStack.getItem() == visibleStack.getItem()) {
                                visibleStackList.add(visibleStack);
                            }
                        }
                    }
                }
                visibleStacks = visibleStackList.toArray(new ItemStack[visibleStackList.size()]);
            }
            visibleItemTime = ITEM_SWITCH_TIME;
            visibleItemIndex = 0;
        } else {
            visibleStacks = null;
            putStack(null);
        }
    }

    public void update() {
        if(visibleStacks != null && visibleStacks.length > 0) {
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
