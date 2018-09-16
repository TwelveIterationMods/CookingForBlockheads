package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

public class FakeSlotCraftMatrix extends FakeSlot {

    private static final float ITEM_SWITCH_TIME = 80f;

    private final NonNullList<ItemStack> visibleStacks = NonNullList.create();

    private float visibleItemTime;
    private int visibleItemIndex;
    private boolean isLocked;

    public FakeSlotCraftMatrix(int slotId, int x, int y) {
        super(slotId, x, y);
    }

    public void setIngredient(@Nullable NonNullList<ItemStack> ingredients) {
        ItemStack prevLockStack = isLocked ? getStack() : ItemStack.EMPTY;
        visibleStacks.clear();
        if (ingredients != null) {
            for (ItemStack itemStack : ingredients) {
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        NonNullList<ItemStack> subItems = NonNullList.create();
                        CreativeTabs tab = itemStack.getItem().getCreativeTab();
                        if (tab != null) {
                            itemStack.getItem().getSubItems(tab, subItems);
                        }
                        visibleStacks.addAll(subItems);
                    } else {
                        itemStack.setCount(1);
                        visibleStacks.add(itemStack);
                    }
                }
            }
        }

        visibleItemTime = 0;
        visibleItemIndex = !visibleStacks.isEmpty() ? slotNumber % visibleStacks.size() : 0;
        isLocked = false;

        if (!prevLockStack.isEmpty()) {
            for (int i = 0; i < visibleStacks.size(); i++) {
                if (ItemUtils.areItemStacksEqualWithWildcard(visibleStacks.get(i), prevLockStack)) {
                    visibleItemIndex = i;
                    isLocked = true;
                }
            }
        }
    }

    public void updateSlot(float partialTicks) {
        if (!isLocked) {
            visibleItemTime += partialTicks;
            if (visibleItemTime >= ITEM_SWITCH_TIME) {
                visibleItemIndex++;
                if (visibleItemIndex >= visibleStacks.size()) {
                    visibleItemIndex = 0;
                }
                visibleItemTime = 0;
            }
        }
    }

    @Override
    public ItemStack getStack() {
        return visibleStacks.size() > 0 ? visibleStacks.get(visibleItemIndex) : ItemStack.EMPTY;
    }

    @Override
    public boolean getHasStack() {
        return visibleStacks.size() > 0;
    }

    @Override
    public boolean isEnabled() {
        return visibleStacks.size() > 0;
    }

    public NonNullList<ItemStack> getVisibleStacks() {
        return visibleStacks;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void scrollDisplayList(int i) {
        isLocked = true;
        visibleItemIndex += i;
        if (visibleItemIndex >= visibleStacks.size()) {
            visibleItemIndex = 0;
        } else if (visibleItemIndex < 0) {
            visibleItemIndex = visibleStacks.size() - 1;
        }
    }

}
