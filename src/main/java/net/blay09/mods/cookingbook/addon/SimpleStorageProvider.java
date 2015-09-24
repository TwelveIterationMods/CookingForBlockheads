package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.api.kitchen.IKitchenStorageProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

class SimpleStorageProvider implements IKitchenStorageProvider {
    private final IInventory inventory;

    public SimpleStorageProvider(TileEntity tileEntity) {
        this.inventory = (IInventory) tileEntity;
    }

    @Override
    public IInventory getInventory() {
        return inventory;
    }
}
