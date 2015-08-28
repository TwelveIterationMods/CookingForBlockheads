package net.blay09.mods.cookingbook.api.kitchen;

import net.minecraft.inventory.IInventory;

public interface IKitchenStorageProvider extends IMultiblockKitchen {

    IInventory getInventory();

}
