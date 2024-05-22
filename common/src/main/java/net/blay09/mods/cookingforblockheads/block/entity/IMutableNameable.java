package net.blay09.mods.cookingforblockheads.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

public interface IMutableNameable extends Nameable { // TODO 1.20.6 CustomName component
    void setCustomName(Component customName);
    Component getDefaultName();
}
