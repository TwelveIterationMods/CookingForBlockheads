package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraftforge.items.IItemHandler;

public class KitchenItemProvider implements IKitchenItemProvider {

	private IItemHandler itemHandler;

	public KitchenItemProvider() {
	}

	public KitchenItemProvider(IItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	public void setItemHandler(IItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	@Override
	public IItemHandler getItemHandler() {
		return itemHandler;
	}

}
