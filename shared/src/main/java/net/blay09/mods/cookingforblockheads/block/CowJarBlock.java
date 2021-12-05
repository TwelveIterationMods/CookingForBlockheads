package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CowJarBlock extends MilkJarBlock {

    public static final String name = "cow_jar";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CowJarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.cowJar.get(), CowJarBlockEntity::serverTick);
    }
}
