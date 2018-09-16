package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface IKitchenItemProvider {
	void resetSimulation();

	@Deprecated
	ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket);

	int getSimulatedUseCount(int slot);

	ItemStack returnItemStack(ItemStack itemStack);

	@Deprecated
	int getSlots();

	@Deprecated
	ItemStack getStackInSlot(int slot);

	default ItemStack find(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
		return findAndMarkAsUsed(checkStack, maxAmount, inventories, requireBucket, simulate);
	}

	ItemStack findAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	@Nullable
	default SourceItem findSource(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
		return findSourceAndMarkAsUsed(checkStack, maxAmount, inventories, requireBucket, simulate);
	}

	@Nullable
	SourceItem findSourceAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	void consumeSourceItem(SourceItem sourceItem, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireContainer);

	default void markAsUsed(SourceItem sourceItem, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket) {
		useItemStack(sourceItem.getSourceSlot(), Math.max(sourceItem.getSourceStack().getCount(), maxAmount), true, inventories, requireBucket);
	}
}
