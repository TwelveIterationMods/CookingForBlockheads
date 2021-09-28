package net.blay09.mods.cookingforblockheads.tile;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CabinetTileEntity extends CounterTileEntity {

    public CabinetTileEntity() {
        super(ModTileEntities.cabinet);
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.cookingforblockheads.cabinet");
    }
}
