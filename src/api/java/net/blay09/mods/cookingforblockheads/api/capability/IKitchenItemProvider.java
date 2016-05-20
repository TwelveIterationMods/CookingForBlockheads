package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface IKitchenItemProvider {
	void resetSimulation();
	@Nullable
	ItemStack useItemStack(int slot, int amount, boolean simulate);
	@Nullable
	ItemStack returnItemStack(ItemStack itemStack);
	int getSlots();
	@Nullable
	ItemStack getStackInSlot(int slot);
}
