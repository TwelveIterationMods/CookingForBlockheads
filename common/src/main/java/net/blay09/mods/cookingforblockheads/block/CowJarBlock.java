package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CowJarBlock extends MilkJarBlock {

    public CowJarBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CowJarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, ModBlockEntities.cowJar.value(), CowJarBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.cowJar.value(), CowJarBlockEntity::serverTick);
    }

}
