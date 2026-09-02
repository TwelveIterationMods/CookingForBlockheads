package net.blay09.mods.cookingforblockheads.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class KitchenFloorBlock extends Block {

    private final DyeColor color;

    public KitchenFloorBlock(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }
}
