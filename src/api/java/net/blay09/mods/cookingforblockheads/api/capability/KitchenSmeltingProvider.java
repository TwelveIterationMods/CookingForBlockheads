package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.item.ItemStack;

public class KitchenSmeltingProvider implements IKitchenSmeltingProvider {
	@Override
	public ItemStack smeltItem(ItemStack itemStack) {
		return itemStack;
	}
}
