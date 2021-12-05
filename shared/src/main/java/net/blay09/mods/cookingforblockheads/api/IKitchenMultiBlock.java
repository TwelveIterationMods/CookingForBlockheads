package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IKitchenMultiBlock {
    List<IKitchenItemProvider> getItemProviders(Inventory playerInventory);

    ItemStack smeltItem(ItemStack itemStack, int count);

    boolean hasSmeltingProvider();
}
