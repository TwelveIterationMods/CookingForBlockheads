package net.blay09.mods.cookingforblockheads.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftMatrixFakeSlot extends AbstractFakeSlot {

    private int ingredientIndex;
    private List<ItemStack> options = new ArrayList<>();
    private int optionIndex;
    private boolean missing = true;

    public CraftMatrixFakeSlot(Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
    }

    public void setIngredient(final int ingredientIndex, final List<ItemStack> options, final int optionIndex) {
        this.ingredientIndex = ingredientIndex;

        this.options.clear();
        for (final var itemStack : options) {
            this.options.add(itemStack.copyWithCount(1));
        }
        this.optionIndex = optionIndex;
        setDisplayStack(optionIndex >= 0 && optionIndex < this.options.size() ? this.options.get(optionIndex) : ItemStack.EMPTY);
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public boolean isMissing() {
        return missing;
    }

    @Override
    public boolean isActive() {
        return hasItem();
    }

    public ItemStack scrollDisplayList(int i) {
        optionIndex = (optionIndex + i + options.size()) % options.size();
        return options.get(optionIndex);
    }

    public int getIngredientIndex() {
        return ingredientIndex;
    }

    public boolean hasMultipleOptions() {
        return options.size() > 1;
    }
}
