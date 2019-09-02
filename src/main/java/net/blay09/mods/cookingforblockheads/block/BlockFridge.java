package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.*;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockFridge extends BlockKitchen {

    public static final String name = "fridge";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public enum FridgeType implements IStringSerializable {
        SMALL,
        LARGE,
        INVISIBLE;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<FridgeType> TYPE = EnumProperty.create("type", FridgeType.class);
    public static final BooleanProperty PRESERVATION_CHAMBER = BooleanProperty.create("preservation_chamber");
    public static final BooleanProperty ICE_UNIT = BooleanProperty.create("ice_unit");

    public BlockFridge() {
        super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(5f, 10f));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, FLIPPED, PRESERVATION_CHAMBER, ICE_UNIT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getBlockState(pos.up()).getBlock() == this) {
            state = state.withProperty(TYPE, FridgeType.LARGE);
        } else if (world.getBlockState(pos.down()).getBlock() == this) {
            state = state.withProperty(TYPE, FridgeType.INVISIBLE);
        } else {
            state = state.withProperty(TYPE, FridgeType.SMALL);
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        boolean hasPreservationUpgrade = tileEntity instanceof TileFridge && ((TileFridge) tileEntity).hasPreservationUpgrade();
        state = state.withProperty(PRESERVATION_CHAMBER, hasPreservationUpgrade);

        boolean hasIceUpgrade = tileEntity instanceof TileFridge && ((TileFridge) tileEntity).hasIceUpgrade();
        state = state.withProperty(ICE_UNIT, hasIceUpgrade);

        return state;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return (layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFridge();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && heldItem.getItem() == Items.DYE) {
            if (recolorBlock(world, pos, facing, DyeColor.byDyeDamage(heldItem.getItemDamage()))) {
                heldItem.shrink(1);
            }
            return true;
        }

        Direction frontFace = state.get(FACING);
        if (facing == frontFace) {
            TileFridge tileFridge = (TileFridge) world.getTileEntity(pos);
            if (tileFridge != null) {
                if (player.isSneaking()) {
                    tileFridge.getBaseFridge().getDoorAnimator().toggleForcedOpen();
                    return true;
                } else if (!heldItem.isEmpty() && tileFridge.getBaseFridge().getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileFridge.getCombinedItemHandler(), heldItem, false);
                    player.setHeldItem(hand, heldItem);
                    return true;
                }
            }
        }

        if (!world.isRemote) {
            if (!heldItem.isEmpty() && Block.getBlockFromItem(heldItem.getItem()) == ModBlocks.fridge && facing != frontFace) {
                return false;
            }

            player.openGui(CookingForBlockheads.instance, GuiHandler.FRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        boolean below = world.getBlockState(pos.down()).getBlock() == ModBlocks.fridge;
        boolean above = world.getBlockState(pos.up()).getBlock() == ModBlocks.fridge;
        return !(below && above)
                && !(below && world.getBlockState(pos.down(2)).getBlock() == ModBlocks.fridge)
                && !(above && world.getBlockState(pos.up(2)).getBlock() == ModBlocks.fridge)
                && super.isValidPosition(state, world, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.with(FLIPPED, shouldBePlacedFlipped(context, state.get(FACING)));
    }

    @Override
    protected boolean isDyeable() {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFridge) {
            if (((TileFridge) tileEntity).hasIceUpgrade()) {
                ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.iceUnit));
            }

            if (((TileFridge) tileEntity).hasPreservationUpgrade()) {
                ItemUtils.spawnItemStack(world, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.preservationChamber));
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean recolorBlock(BlockState state, IWorld world, BlockPos pos, Direction facing, DyeColor color) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFridge) {
            TileFridge tileFridge = (TileFridge) tileEntity;
            if (tileFridge.getFridgeColor() == color) {
                return false;
            }

            tileFridge.setFridgeColor(color);
            TileFridge neighbourFridge = tileFridge.findNeighbourFridge();
            if (neighbourFridge != null) {
                neighbourFridge.setFridgeColor(color);
            }
        }

        return true;
    }

}
