package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tile.CuttingBoardTileEntity;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;

// TODO drop pam's cutting board
public class BlockCuttingBoard extends BlockKitchen {

    public static final String name = "cutting_board";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final VoxelShape SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 1.6, 14);

    public BlockCuttingBoard() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5f), registryName);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (shouldBlockRenderLowered(world, pos)) {
            return SHAPE.withOffset(0, -0.05, 0);
        }

        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CuttingBoardTileEntity();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Compat.cuttingBoardItem);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        if (!ModList.get().isLoaded(Compat.PAMS_HARVESTCRAFT)) {
            tooltip.add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:requires_pams", TextFormatting.RED));
        }
    }

}
