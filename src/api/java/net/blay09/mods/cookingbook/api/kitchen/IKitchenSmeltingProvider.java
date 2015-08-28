package net.blay09.mods.cookingbook.api.kitchen;

import net.minecraft.item.ItemStack;

public interface IKitchenSmeltingProvider extends IMultiblockKitchen {

    ItemStack smeltItem(ItemStack itemStack);

}
