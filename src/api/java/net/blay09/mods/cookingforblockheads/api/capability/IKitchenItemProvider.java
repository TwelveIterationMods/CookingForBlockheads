package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IKitchenItemProvider {
	void resetSimulation();

	@Deprecated
	ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket);

	default int getCountInSlot(int slot) {
		return getStackInSlot(slot).getCount();
	}

	int getSimulatedUseCount(int slot);

	@Deprecated
	ItemStack returnItemStack(ItemStack itemStack);

	default ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
		return returnItemStack(itemStack);
	}

	@Deprecated
	int getSlots();

	@Deprecated
	ItemStack getStackInSlot(int slot);

	@Deprecated
	default ItemStack findAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
		SourceItem sourceItem = findSourceAndMarkAsUsed(checkStack, maxAmount, inventories, requireBucket, simulate);
		return sourceItem != null ? sourceItem.getSourceStack() :ItemStack.EMPTY;
	}

	@Nullable
	default SourceItem findSource(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
		return findSourceAndMarkAsUsed(checkStack, maxAmount, inventories, requireBucket, simulate);
	}

	@Nullable
	SourceItem findSourceAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	void consumeSourceItem(SourceItem sourceItem, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireContainer);

	default void markAsUsed(SourceItem sourceItem, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket) {
		useItemStack(sourceItem.getSourceSlot(), Math.min(sourceItem.getSourceStack().getCount(), maxAmount), true, inventories, requireBucket);
	}
}
