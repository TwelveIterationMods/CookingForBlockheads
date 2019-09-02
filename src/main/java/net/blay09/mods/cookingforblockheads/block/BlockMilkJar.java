package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMilkJar extends BlockKitchen {

    public static final String name = "milk_jar";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.5, 0.7);

    public BlockMilkJar() {
        super(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.6f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(LOWERED, shouldBlockRenderLowered(world, pos));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (shouldBlockRenderLowered(world, pos)) {
            return BOUNDING_BOX.expand(0, -0.05, 0);
        }

        return BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileMilkJar tileMilkJar = (TileMilkJar) world.getTileEntity(pos);
        if (!heldItem.isEmpty() && tileMilkJar != null) {
            if (heldItem.getItem() == Items.MILK_BUCKET) {
                if (tileMilkJar.getMilkAmount() <= tileMilkJar.getMilkCapacity() - 1000) {
                    tileMilkJar.fill(1000, true);
                    if (!player.abilities.isCreativeMode) {
                        player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                    }
                }
                return true;
            } else if (heldItem.getItem() == Items.BUCKET) {
                if (tileMilkJar.getMilkAmount() >= 1000) {
                    if (heldItem.getCount() == 1) {
                        tileMilkJar.drain(1000, true);
                        if (!player.abilities.isCreativeMode) {
                            player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
                        }
                    } else {
                        if (player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                            tileMilkJar.drain(1000, true);
                            if (!player.abilities.isCreativeMode) {
                                heldItem.shrink(1);
                            }
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMilkJar();
    }

}
