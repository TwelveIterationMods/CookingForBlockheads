package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.FridgeBlockEntity;
import net.blay09.mods.cookingforblockheads.tile.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FridgeBlock extends BlockDyeableKitchen {

    public static final String name = "fridge";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public enum FridgeModelType implements StringRepresentable {
        SMALL,
        LARGE_LOWER,
        LARGE_UPPER;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<FridgeModelType> MODEL_TYPE = EnumProperty.create("model", FridgeModelType.class);
    public static final BooleanProperty PRESERVATION_CHAMBER = BooleanProperty.create("preservation_chamber");
    public static final BooleanProperty ICE_UNIT = BooleanProperty.create("ice_unit");

    public FridgeBlock() {
        super(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(5f, 10f), registryName);
        registerDefaultState(getStateDefinition().any().setValue(PRESERVATION_CHAMBER, false).setValue(ICE_UNIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODEL_TYPE, FLIPPED, PRESERVATION_CHAMBER, ICE_UNIT, COLOR, HAS_COLOR);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FridgeBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.getItem() == ModItems.preservationChamber || heldItem.getItem() == ModItems.iceUnit) {
            return InteractionResult.PASS;
        }

        if (tryRecolorBlock(state, heldItem, level, pos, player, rayTraceResult)) {
            return InteractionResult.SUCCESS;
        }

        FridgeBlockEntity fridge = (FridgeBlockEntity) level.getBlockEntity(pos);
        Direction frontFace = state.getValue(FACING);
        if (rayTraceResult.getDirection() == frontFace) {
            if (fridge != null) {
                if (player.isShiftKeyDown()) {
                    fridge.getBaseFridge().getDoorAnimator().toggleForcedOpen();
                    return InteractionResult.SUCCESS;
                } else if (!heldItem.isEmpty() && fridge.getBaseFridge().getDoorAnimator().isForcedOpen()) {
                    heldItem = fridge.insertItemStacked(heldItem, false);
                    player.setItemInHand(hand, heldItem);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (!level.isClientSide) {
            if (!heldItem.isEmpty() && Block.byItem(heldItem.getItem()) instanceof FridgeBlock && rayTraceResult.getDirection() != frontFace) {
                return InteractionResult.FAIL;
            }

            Balm.getNetworking().openGui(player, fridge);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean below = level.getBlockState(pos.below()).getBlock() instanceof FridgeBlock;
        boolean above = level.getBlockState(pos.above()).getBlock() instanceof FridgeBlock;
        return !(below && above)
                && !(below && level.getBlockState(pos.below(2)).getBlock() instanceof FridgeBlock)
                && !(above && level.getBlockState(pos.above(2)).getBlock() instanceof FridgeBlock)
                && super.canSurvive(state, level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(FLIPPED, shouldBePlacedFlipped(context, state.getValue(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockPos posBelow = currentPos.below();
        BlockState stateBelow = world.getBlockState(posBelow);
        BlockPos posAbove = currentPos.above();
        BlockState stateAbove = world.getBlockState(posAbove);
        if (stateBelow.getBlock() == ModBlocks.fridge && stateBelow.getValue(MODEL_TYPE) == FridgeModelType.SMALL) {
            world.setBlock(posBelow, stateBelow.setValue(MODEL_TYPE, FridgeModelType.LARGE_LOWER).setValue(FACING, state.getValue(FACING)), 3);
            return state.setValue(MODEL_TYPE, FridgeModelType.LARGE_UPPER);
        } else if (stateAbove.getBlock() == ModBlocks.fridge && stateAbove.getValue(MODEL_TYPE) == FridgeModelType.SMALL) {
            world.setBlock(posAbove, stateAbove.setValue(MODEL_TYPE, FridgeModelType.LARGE_UPPER).setValue(FACING, state.getValue(FACING)), 3);
            return state.setValue(MODEL_TYPE, FridgeModelType.LARGE_LOWER);
        }

        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    protected boolean isDyeable() {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FridgeBlockEntity) {
                if (((FridgeBlockEntity) blockEntity).hasIceUpgrade()) {
                    ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.iceUnit));
                }

                if (((FridgeBlockEntity) blockEntity).hasPreservationUpgrade()) {
                    ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.preservationChamber));
                }
            }

            BlockPos posAbove = pos.above();
            BlockState stateAbove = level.getBlockState(posAbove);
            BlockPos posBelow = pos.below();
            BlockState stateBelow = level.getBlockState(posBelow);
            if (stateAbove.getBlock() == ModBlocks.fridge && stateAbove.getValue(MODEL_TYPE) == FridgeModelType.LARGE_UPPER) {
                level.setBlock(posAbove, stateAbove.setValue(MODEL_TYPE, FridgeModelType.SMALL), 3);
            } else if (stateBelow.getBlock() == ModBlocks.fridge && stateBelow.getValue(MODEL_TYPE) == FridgeModelType.LARGE_LOWER) {
                level.setBlock(posBelow, stateBelow.setValue(MODEL_TYPE, FridgeModelType.SMALL), 3);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide
                ? createTickerHelper(type, ModBlockEntities.fridge.get(), FridgeBlockEntity::clientTick)
                : createTickerHelper(type, ModBlockEntities.fridge.get(), FridgeBlockEntity::serverTick);
    }

    @Override
    protected boolean recolorBlock(BlockState state, LevelAccessor world, BlockPos pos, Direction facing, DyeColor color) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FridgeBlockEntity fridge) {
            BlockPos bottomPos = fridge.getBaseFridge().getBlockPos();
            BlockPos topPos = bottomPos.above();
            return super.recolorBlock(world.getBlockState(bottomPos), world, bottomPos, facing, color) && super.recolorBlock(world.getBlockState(topPos), world, topPos, facing, color);
        }

        return false;
    }
}
