package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.MilkJarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class MilkJarBlock extends BlockKitchen {

    public static final String name = "milk_jar";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    private static final VoxelShape SHAPE = Block.makeCuboidShape(4.8, 0, 4.8, 11.2, 8.0, 11.2);

    public MilkJarBlock() {
        super(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.6f), registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (shouldBlockRenderLowered(world, pos)) {
            return SHAPE.withOffset(0, -0.05, 0);
        }

        return SHAPE;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        MilkJarTileEntity tileMilkJar = (MilkJarTileEntity) world.getTileEntity(pos);
        if (!heldItem.isEmpty() && tileMilkJar != null) {
            if (heldItem.getItem() == Items.MILK_BUCKET) {
                if (tileMilkJar.getMilkAmount() <= tileMilkJar.getMilkCapacity() - 1000) {
                    tileMilkJar.fill(1000, IFluidHandler.FluidAction.EXECUTE);
                    if (!player.abilities.isCreativeMode) {
                        player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                    }
                }
                return true;
            } else if (heldItem.getItem() == Items.BUCKET) {
                if (tileMilkJar.getMilkAmount() >= 1000) {
                    if (heldItem.getCount() == 1) {
                        tileMilkJar.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        if (!player.abilities.isCreativeMode) {
                            player.setHeldItem(hand, new ItemStack(Items.MILK_BUCKET));
                        }
                    } else {
                        if (player.inventory.addItemStackToInventory(new ItemStack(Items.MILK_BUCKET))) {
                            tileMilkJar.drain(1000, IFluidHandler.FluidAction.EXECUTE);
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
        return new MilkJarTileEntity();
    }

}
