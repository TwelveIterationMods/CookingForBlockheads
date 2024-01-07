package net.blay09.mods.cookingforblockheads.api.capability;

import net.minecraft.world.item.ItemStack;

@Deprecated(forRemoval = true)
public interface IKitchenSmeltingProvider {
	ItemStack smeltItem(ItemStack itemStack);
}
