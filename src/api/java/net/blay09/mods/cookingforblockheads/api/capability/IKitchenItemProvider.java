package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IKitchenItemProvider {
	void resetSimulation();
	ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket);
	ItemStack returnItemStack(ItemStack itemStack);
	int getSlots();
	ItemStack getStackInSlot(int slot);
}
