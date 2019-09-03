package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.tile.CuttingBoardTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockCuttingBoard extends BlockKitchen {

    public static final String name = "cutting_board";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.1, 0.875);

    public BlockCuttingBoard() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5f), registryName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos) {
        if (shouldBlockRenderLowered(world, pos)) {
            return BOUNDING_BOX.expand(0, -0.05, 0);
        }

        return BOUNDING_BOX;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getActualState(BlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(LOWERED, shouldBlockRenderLowered(world, pos));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CuttingBoardTileEntity();
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Compat.cuttingBoardItem;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Compat.cuttingBoardItem);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        if (!ModList.get().isLoaded(Compat.PAMS_HARVESTCRAFT)) {
            tooltip.add(TextFormatting.RED + I18n.format("tooltip.cookingforblockheads:requires_pams"));
        } else {
            for (String s : I18n.format("tooltip.cookingforblockheads:cutting_board_deprecated").split("\\\\n")) {
                tooltip.add(TextFormatting.RED + s);
            }
        }
    }

}
