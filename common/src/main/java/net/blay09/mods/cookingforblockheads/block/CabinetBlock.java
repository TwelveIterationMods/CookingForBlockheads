package net.blay09.mods.cookingforblockheads.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.cookingforblockheads.block.entity.CabinetBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CabinetBlock extends CounterBlock {

    public static final MapCodec<CabinetBlock> CODEC = RecordCodecBuilder.mapCodec((it) -> it.group(DyeColor.CODEC.fieldOf("color")
                    .orElse(null)
                    .forGetter(CabinetBlock::getColor),
            propertiesCodec()).apply(it, CabinetBlock::new));

    private static final VoxelShape BOUNDING_BOX_NORTH = Block.box(0, 2, 2, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_EAST = Block.box(0, 2, 0, 14, 16, 16);
    private static final VoxelShape BOUNDING_BOX_WEST = Block.box(2, 2, 0, 16, 16, 16);
    private static final VoxelShape BOUNDING_BOX_SOUTH = Block.box(0, 2, 0, 16, 16, 14);

    public CabinetBlock(Properties properties) {
        this(null, properties);
    }

    public CabinetBlock(@Nullable DyeColor color, Properties properties) {
        super(color, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> BOUNDING_BOX_EAST;
            case WEST -> BOUNDING_BOX_WEST;
            case SOUTH -> BOUNDING_BOX_SOUTH;
            default -> BOUNDING_BOX_NORTH;
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? createTickerHelper(type, ModBlockEntities.cabinet.value(), CabinetBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.cabinet.value(), CabinetBlockEntity::serverTick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState getDyedStateOf(BlockState state, @Nullable DyeColor color) {
        return ModBlocks.cabinets.getDeferred(color)
                .defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(FLIPPED, state.getValue(FLIPPED));
    }
}
