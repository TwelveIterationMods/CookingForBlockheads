package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMilkJar extends BlockKitchen {

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.5, 0.7);

    public BlockMilkJar() {
        super(Material.GLASS);

        setSoundType(SoundType.GLASS);
        setHardness(0.6f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, LOWERED);
    }

    @Override
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        TileMilkJar tileMilkJar = (TileMilkJar) world.getTileEntity(pos);
        if (!heldItem.isEmpty() && tileMilkJar != null) {
            if (heldItem.getItem() == Items.MILK_BUCKET) {
                if (tileMilkJar.getMilkAmount() <= tileMilkJar.getMilkCapacity() - 1000) {
                    tileMilkJar.fill(1000, true);
                    player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                return true;
            } else if (heldItem.getItem() == Items.BUCKET) {
                if (tileMilkJar.getMilkAmount() >= 1000) {
                    if (heldItem.getCount() == 1) {
                        tileMilkJar.drain(1000, true);
                        player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
                    } else {
                        if (player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                            tileMilkJar.drain(1000, true);
                            heldItem.shrink(1);
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileMilkJar();
    }

    @Override
    public String getIdentifier() {
        return "milk_jar";
    }

    @Nullable
    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileMilkJar.class;
    }

}
