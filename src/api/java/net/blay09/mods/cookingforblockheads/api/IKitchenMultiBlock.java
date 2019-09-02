package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IKitchenMultiBlock {
    List<IKitchenItemProvider> getItemProviders(PlayerInventory playerInventory);

    ItemStack smeltItem(ItemStack itemStack, int count);

    boolean hasSmeltingProvider();
}
