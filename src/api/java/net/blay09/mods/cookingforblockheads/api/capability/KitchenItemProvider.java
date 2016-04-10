package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class KitchenItemProvider implements IKitchenItemProvider {

	private IItemHandler itemHandler;
	private int[] usedStackSize;

	public KitchenItemProvider() {
	}

	public KitchenItemProvider(IItemHandler itemHandler) {
		this.itemHandler = itemHandler;
		this.usedStackSize = new int[itemHandler.getSlots()];
	}

	public void setItemHandler(IItemHandler itemHandler) {
		this.itemHandler = itemHandler;
		this.usedStackSize = new int[itemHandler.getSlots()];
	}

	@Override
	public void resetSimulation() {
		for(int i = 0; i < usedStackSize.length; i++) {
			usedStackSize[i] = 0;
		}
	}

	@Override
	public ItemStack useItemStack(int slot, int amount, boolean simulate) {
		ItemStack itemStack = itemHandler.getStackInSlot(slot);
		if(itemStack.stackSize - usedStackSize[slot] >= amount) {
			ItemStack result = itemHandler.extractItem(slot, amount, simulate);
			if(simulate && result != null) {
				usedStackSize[slot] += result.stackSize;
			}
			return result;
		}
		return null;
	}

	@Override
	public ItemStack returnItemStack(ItemStack itemStack) {
		return ItemHandlerHelper.insertItemStacked(itemHandler, itemStack, false);
	}

	@Override
	public int getSlots() {
		return itemHandler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return itemHandler.getStackInSlot(slot);
	}

}
