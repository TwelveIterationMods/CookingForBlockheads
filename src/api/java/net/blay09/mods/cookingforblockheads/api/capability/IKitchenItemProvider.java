package net.blay09.mods.cookingforblockheads.api.capability;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public interface IKitchenItemProvider {
	void resetSimulation();
	ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket);
	ItemStack returnItemStack(ItemStack itemStack);

	@Deprecated
	int getSlots();

	@Deprecated
	ItemStack getStackInSlot(int slot);

	ItemStack findAndMarkAsUsed(Predicate<ItemStack> checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);

	@Nullable
	SourceItem findSourceAndMarkAsUsed(Predicate<ItemStack> checkStack, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate);
}
