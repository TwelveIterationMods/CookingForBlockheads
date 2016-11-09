package net.blay09.mods.cookingforblockheads.api;

import java.util.List;

import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public interface IKitchenMultiBlock {

    List<IKitchenItemProvider> getItemProviders(InventoryPlayer playerInventory);
    ItemStack smeltItem(ItemStack itemStack, int count);
    boolean hasSmeltingProvider();
    
}
