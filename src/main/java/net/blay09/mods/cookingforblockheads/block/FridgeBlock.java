package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.FridgeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FridgeBlock extends BlockDyeableKitchen {

    public static final String name = "fridge";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public enum FridgeModelType implements IStringSerializable {
        SMALL,
        LARGE_LOWER,
        LARGE_UPPER;

        @Override
        public String getString() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<FridgeModelType> MODEL_TYPE = EnumProperty.create("model", FridgeModelType.class);
    public static final BooleanProperty PRESERVATION_CHAMBER = BooleanProperty.create("preservation_chamber");
    public static final BooleanProperty ICE_UNIT = BooleanProperty.create("ice_unit");

    public FridgeBlock() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5f, 10f), registryName);
        setDefaultState(getStateContainer().getBaseState().with(PRESERVATION_CHAMBER, false).with(ICE_UNIT, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODEL_TYPE, FLIPPED, PRESERVATION_CHAMBER, ICE_UNIT, COLOR, HAS_COLOR);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FridgeTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (tryRecolorBlock(state, heldItem, world, pos, player, rayTraceResult)) {
            return ActionResultType.SUCCESS;
        }

        FridgeTileEntity tileFridge = (FridgeTileEntity) world.getTileEntity(pos);
        Direction frontFace = state.get(FACING);
        if (rayTraceResult.getFace() == frontFace) {
            if (tileFridge != null) {
                if (player.isSneaking()) {
                    tileFridge.getBaseFridge().getDoorAnimator().toggleForcedOpen();
                    return ActionResultType.SUCCESS;
                } else if (!heldItem.isEmpty() && tileFridge.getBaseFridge().getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileFridge.getCombinedItemHandler(), heldItem, false);
                    player.setHeldItem(hand, heldItem);
                    return ActionResultType.SUCCESS;
                }
            }
        }

        if (!world.isRemote) {
            if (!heldItem.isEmpty() && Block.getBlockFromItem(heldItem.getItem()) instanceof FridgeBlock && rayTraceResult.getFace() != frontFace) {
                return ActionResultType.FAIL;
            }

            NetworkHooks.openGui((ServerPlayerEntity) player, tileFridge, pos);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        boolean below = world.getBlockState(pos.down()).getBlock() instanceof FridgeBlock;
        boolean above = world.getBlockState(pos.up()).getBlock() instanceof FridgeBlock;
        return !(below && above)
                && !(below && world.getBlockState(pos.down(2)).getBlock() instanceof FridgeBlock)
                && !(above && world.getBlockState(pos.up(2)).getBlock() instanceof FridgeBlock)
                && super.isValidPosition(state, world, pos);
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FLIPPED, shouldBePlacedFlipped(context, state.get(FACING)));
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        BlockPos posBelow = currentPos.down();
        BlockState stateBelow = world.getBlockState(posBelow);
        BlockPos posAbove = currentPos.up();
        BlockState stateAbove = world.getBlockState(posAbove);
        if (stateBelow.getBlock() == ModBlocks.fridge && stateBelow.get(MODEL_TYPE) == FridgeModelType.SMALL) {
            world.setBlockState(posBelow, stateBelow.with(MODEL_TYPE, FridgeModelType.LARGE_LOWER).with(FACING, state.get(FACING)), 3);
            return state.with(MODEL_TYPE, FridgeModelType.LARGE_UPPER);
        } else if (stateAbove.getBlock() == ModBlocks.fridge && stateAbove.get(MODEL_TYPE) == FridgeModelType.SMALL) {
            world.setBlockState(posAbove, stateAbove.with(MODEL_TYPE, FridgeModelType.LARGE_UPPER).with(FACING, state.get(FACING)), 3);
            return state.with(MODEL_TYPE, FridgeModelType.LARGE_LOWER);
        }

        return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    protected boolean isDyeable() {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() != state.getBlock()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof FridgeTileEntity) {
                if (((FridgeTileEntity) tileEntity).hasIceUpgrade()) {
                    ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.iceUnit));
                }

                if (((FridgeTileEntity) tileEntity).hasPreservationUpgrade()) {
                    ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.preservationChamber));
                }
            }

            BlockPos posAbove = pos.up();
            BlockState stateAbove = world.getBlockState(posAbove);
            BlockPos posBelow = pos.down();
            BlockState stateBelow = world.getBlockState(posBelow);
            if (stateAbove.getBlock() == ModBlocks.fridge && stateAbove.get(MODEL_TYPE) == FridgeModelType.LARGE_UPPER) {
                world.setBlockState(posAbove, stateAbove.with(MODEL_TYPE, FridgeModelType.SMALL), 3);
            } else if (stateBelow.getBlock() == ModBlocks.fridge && stateBelow.get(MODEL_TYPE) == FridgeModelType.LARGE_LOWER) {
                world.setBlockState(posBelow, stateBelow.with(MODEL_TYPE, FridgeModelType.SMALL), 3);
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

}
