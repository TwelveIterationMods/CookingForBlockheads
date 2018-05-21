package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

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

    public static final PropertyEnum<FridgeType> TYPE = PropertyEnum.create("type", FridgeType.class);

    public BlockFridge() {
        super(Material.IRON);

        setUnlocalizedName(registryName.toString());
        setSoundType(SoundType.METAL);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, TYPE, FLIPPED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing;
        switch (meta & 7) {
            case 0:
                facing = EnumFacing.EAST;
                break;
            case 1:
                facing = EnumFacing.WEST;
                break;
            case 2:
                facing = EnumFacing.SOUTH;
                break;
            case 3:
            default:
                facing = EnumFacing.NORTH;
                break;
        }
        return getDefaultState().withProperty(FACING, facing).withProperty(FLIPPED, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta;
        switch (state.getValue(FACING)) {
            case EAST:
                meta = 0;
                break;
            case WEST:
                meta = 1;
                break;
            case SOUTH:
                meta = 2;
                break;
            case NORTH:
            default:
                meta = 3;
                break;
        }
        if (state.getValue(FLIPPED)) {
            meta |= 8;
        }
        return meta;
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
        return state;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return (layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileFridge();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && heldItem.getItem() == Items.DYE) {
            if (recolorBlock(world, pos, facing, EnumDyeColor.byDyeDamage(heldItem.getItemDamage()))) {
                heldItem.shrink(1);
            }
            return true;
        }

        if (facing == state.getValue(FACING)) {
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
            if (!heldItem.isEmpty() && Block.getBlockFromItem(heldItem.getItem()) == ModBlocks.fridge) {
                return false;
            }

            player.openGui(CookingForBlockheads.instance, GuiHandler.FRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        boolean below = world.getBlockState(pos.down()).getBlock() == ModBlocks.fridge;
        boolean above = world.getBlockState(pos.up()).getBlock() == ModBlocks.fridge;
        return !(below && above)
                && !(below && world.getBlockState(pos.down(2)).getBlock() == ModBlocks.fridge)
                && !(above && world.getBlockState(pos.up(2)).getBlock() == ModBlocks.fridge)
                && super.canPlaceBlockAt(world, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer);
        return state.withProperty(FLIPPED, shouldBePlacedFlipped(pos, state.getValue(FACING), placer));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
        tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.cookingforblockheads:dyeable"));
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
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
