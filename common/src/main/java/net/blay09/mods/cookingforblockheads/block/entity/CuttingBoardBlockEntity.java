package net.blay09.mods.cookingforblockheads.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardBlockEntity extends BlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cuttingBoard.value(), pos, state);
    }

}
