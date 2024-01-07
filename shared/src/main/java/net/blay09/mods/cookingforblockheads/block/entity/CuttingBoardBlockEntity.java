package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.common.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardBlockEntity extends BalmBlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cuttingBoard.get(), pos, state);
    }

}
