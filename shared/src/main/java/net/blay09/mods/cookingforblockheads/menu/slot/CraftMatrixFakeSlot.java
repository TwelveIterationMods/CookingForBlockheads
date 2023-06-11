package net.blay09.mods.cookingforblockheads.menu.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CraftMatrixFakeSlot extends FakeSlot {

    private static final float ITEM_SWITCH_TIME = 80f;

    private final NonNullList<ItemStack> visibleStacks = NonNullList.create();

    private float visibleItemTime;
    private int visibleItemIndex;
    private boolean isLocked;
    private boolean available = true;

    public CraftMatrixFakeSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    public void setIngredient(@Nullable NonNullList<ItemStack> ingredients) {
        ItemStack prevLockStack = isLocked ? getItem() : ItemStack.EMPTY;
        visibleStacks.clear();
        if (ingredients != null) {
            for (ItemStack itemStack : ingredients) {
                if (!itemStack.isEmpty()) {
                    itemStack.setCount(1);
                    visibleStacks.add(itemStack);
                }
            }
        }

        visibleItemTime = 0;
        visibleItemIndex = !visibleStacks.isEmpty() ? index % visibleStacks.size() : 0;
        isLocked = false;

        if (!prevLockStack.isEmpty()) {
            for (int i = 0; i < visibleStacks.size(); i++) {
                if (ItemStack.isSameItem(visibleStacks.get(i), prevLockStack)) {
                    visibleItemIndex = i;
                    isLocked = true;
                }
            }
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
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
    public ItemStack getItem() {
        return visibleStacks.size() > 0 ? visibleStacks.get(visibleItemIndex) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return visibleStacks.size() > 0;
    }

    @Override
    public boolean isActive() {
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
