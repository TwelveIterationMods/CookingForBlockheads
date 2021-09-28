package net.blay09.mods.cookingforblockheads.tile;

import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

public interface IMutableNameable extends INameable {
    void setCustomName(ITextComponent customName);
    ITextComponent getDefaultName();
}
