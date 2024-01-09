package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.common.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CornerBlockEntity extends BalmBlockEntity {

    public CornerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.corner.get(), pos, state);
    }

}
