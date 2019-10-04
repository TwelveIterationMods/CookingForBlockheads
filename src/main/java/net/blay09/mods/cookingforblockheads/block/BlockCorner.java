package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.TileCorner;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockCorner extends BlockKitchen {

    public static final String name = "corner";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[]{
            new AxisAlignedBB(0, 0, 0, 0.96875, 0.9375, 0.96875),
            new AxisAlignedBB(0.03125, 0, 0.03125, 1, 0.9375, 1),
            new AxisAlignedBB(0, 0, 0.03125, 0.96875, 0.9375, 1),
            new AxisAlignedBB(0.03125, 0, 0, 1, 0.9375, 0.96875)
    };

    public BlockCorner() {
        super(Material.ROCK);

        setUnlocalizedName(registryName.toString());
        setSoundType(SoundType.STONE);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, COLOR);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCorner) {
            return state.withProperty(COLOR, ((TileCorner) tileEntity).getDyedColor());
        }

        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!heldItem.isEmpty() && DyeUtils.isDye(heldItem)) {
			Optional<EnumDyeColor> dyeColor = DyeUtils.colorFromStack(heldItem);
			if (dyeColor.isPresent() && recolorBlock(world, pos, facing, dyeColor.get())) {
				if (!player.isCreative()) {
					heldItem.shrink(1);
				}
			}
			return true;
		}

        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOXES[state.getValue(FACING).getIndex() - 2];
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getFront(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCorner();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);

        for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
