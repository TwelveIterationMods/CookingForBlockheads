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

	ItemStack findAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	@Nullable
	SourceItem findSourceAndMarkAsUsed(IngredientPredicate checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	void consumeSourceItem(SourceItem sourceItem, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireContainer);
}
