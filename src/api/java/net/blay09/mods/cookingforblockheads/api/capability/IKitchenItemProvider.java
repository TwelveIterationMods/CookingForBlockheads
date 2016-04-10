package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IKitchenItemProvider {
	void resetSimulation();
	ItemStack useItemStack(int slot, int amount, boolean simulate);
	ItemStack returnItemStack(ItemStack itemStack);
	int getSlots();
	ItemStack getStackInSlot(int slot);
}
