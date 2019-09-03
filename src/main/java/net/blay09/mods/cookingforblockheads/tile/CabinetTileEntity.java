package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;

public class CabinetTileEntity extends CounterTileEntity {

    public CabinetTileEntity() {
        super(ModTileEntities.cabinet);
    }

    @Override
    public String getUnlocalizedName() {
        return CookingForBlockheads.MOD_ID + ":cabinet";
    }
}
