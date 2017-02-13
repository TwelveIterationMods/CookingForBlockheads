package net.blay09.mods.cookingforblockheads.container;

import invtweaks.api.container.ChestContainer;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

@ChestContainer
public class ContainerSpiceRack extends Container {

	private final TileSpiceRack tileSpiceRack;

	public ContainerSpiceRack(EntityPlayer player, TileSpiceRack tileSpiceRack) {
		this.tileSpiceRack = tileSpiceRack;
		IItemHandler itemHandler = tileSpiceRack.getItemHandler();

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new SlotItemHandler(itemHandler, i, 8 + i * 18, 18));
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 108));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		// TODO fix me properly
		ItemStack itemStack = null;
		Slot slot = inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			itemStack = slotStack.copy();
			if (slotIndex < 9) {
				if (!this.mergeItemStack(slotStack, 9, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(slotStack, 0, 9, false)) {
				return null;
			}

			if (slotStack.isEmpty()) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
