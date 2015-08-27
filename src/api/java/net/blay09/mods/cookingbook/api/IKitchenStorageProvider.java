package net.blay09.mods.cookingbook.api;

import net.minecraft.inventory.IInventory;

public interface IKitchenStorageProvider extends IMultiblockKitchen {

    IInventory getInventory();
    int[] getAccessibleSlots();

}
