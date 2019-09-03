package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.CabinetTileEntity;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCabinet extends BlockCounter {
    public static final String name = "cabinet";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final AxisAlignedBB BOUNDING_BOX_NORTH = new AxisAlignedBB(0f, 0.125f, 0.125f, 1f, 1f, 1);
    private static final AxisAlignedBB BOUNDING_BOX_EAST = new AxisAlignedBB(0f, 0.125f, 0, 0.875f, 1f, 1);
    private static final AxisAlignedBB BOUNDING_BOX_WEST = new AxisAlignedBB(0.125f, 0.125f, 0, 1f, 1f, 1);
    private static final AxisAlignedBB BOUNDING_BOX_SOUTH = new AxisAlignedBB(0f, 0.125f, 0f, 1f, 1f, 0.875f);

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new CabinetTileEntity();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);

        for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(FACING)) {
            case EAST:
                return BOUNDING_BOX_EAST;
            case WEST:
                return BOUNDING_BOX_WEST;
            case SOUTH:
                return BOUNDING_BOX_SOUTH;
            case NORTH:
            default:
                return BOUNDING_BOX_NORTH;
        }
    }
}
