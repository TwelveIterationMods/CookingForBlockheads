package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.CornerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class KitchenCornerBlock extends BlockDyeableKitchen {

    public static final String name = "corner";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape[] BOUNDING_BOXES = new VoxelShape[]{
            Block.makeCuboidShape(0, 0, 0, 0.96875, 0.9375, 0.96875),
            Block.makeCuboidShape(0.03125, 0, 0.03125, 1, 0.9375, 1),
            Block.makeCuboidShape(0, 0, 0.03125, 0.96875, 0.9375, 1),
            Block.makeCuboidShape(0.03125, 0, 0, 1, 0.9375, 0.96875)
    };

    public KitchenCornerBlock(DyeColor dyeColor, ResourceLocation registryName) {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5f, 10f), dyeColor, registryName);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, COLOR);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (tryRecolorBlock(heldItem, world, pos, player, rayTraceResult)) {
            return true;
        }

        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOXES[state.get(FACING).getIndex() - 2];
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CornerTileEntity();
    }

}
