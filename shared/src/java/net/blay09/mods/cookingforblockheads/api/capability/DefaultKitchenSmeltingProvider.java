package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.world.item.ItemStack;

public class DefaultKitchenSmeltingProvider implements IKitchenSmeltingProvider {
	@Override
	public ItemStack smeltItem(ItemStack itemStack) {
		return itemStack;
	}
}
