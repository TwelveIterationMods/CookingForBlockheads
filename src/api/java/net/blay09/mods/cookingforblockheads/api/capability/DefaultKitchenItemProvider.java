package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class DefaultKitchenItemProvider implements IKitchenItemProvider {

    @Override
    public ItemStack findAndMarkAsUsed(Predicate<ItemStack> predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
        SourceItem sourceItem = findSourceAndMarkAsUsed(predicate, maxAmount, inventories, requireBucket, simulate);
        if (sourceItem != null) {
            return sourceItem.getSourceStack();
        }

        return ItemStack.EMPTY;
    }

    @Override
    @Nullable
    public SourceItem findSourceAndMarkAsUsed(Predicate<ItemStack> predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
        for (int j = 0; j < getSlots(); j++) {
            ItemStack itemStack = getStackInSlot(j);
            int amount = Math.min(itemStack.getCount(), maxAmount);
            if (predicate.test(itemStack)) {
                itemStack = useItemStack(j, amount, simulate, inventories, requireBucket);
                if (!itemStack.isEmpty()) {
                    return new SourceItem(this, j, itemStack);
                }
            }
        }

        return null;
    }

}
