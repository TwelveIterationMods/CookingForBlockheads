package net.blay09.mods.cookingbook.api;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface IKitchenItemProvider extends IMultiblockKitchen {

    List<ItemStack> getProvidedItemStacks();

}
