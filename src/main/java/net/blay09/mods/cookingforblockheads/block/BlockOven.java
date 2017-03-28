package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.blaycommon.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

public class  BlockOven extends BlockKitchen {

    private static final Random random = new Random();

    public BlockOven() {
        super(Material.IRON);

        setRegistryName(CookingForBlockheads.MOD_ID, "oven");
        setUnlocalizedName(getRegistryNameString());
        setSoundType(SoundType.METAL);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
        if(facing == EnumFacing.UP) {
            if(CookingRegistry.isToolItem(heldItem)) {
                EnumFacing stateFacing = state.getValue(FACING);
                float hx = hitX;
                float hz = hitZ;
                switch(stateFacing) {
                    case NORTH: hx = 1f - hitX; hz = 1f - hitZ; break;
//                    case SOUTH: hx = hitX; hz = hitZ; break;
                    case WEST: hz = 1f - hitX; hx = hitZ; break;
                    case EAST: hz = hitX; hx = 1f - hitZ; break;
                }
                int index = -1;
                if(hx < 0.5f && hz < 0.5f) {
                    index = 1;
                } else if(hx >= 0.5f && hz < 0.5f) {
                    index = 0;
                } else if(hx < 0.5f && hz >= 0.5f) {
                    index = 3;
                } else if(hx >= 0.5f && hz >= 0.5f) {
                    index = 2;
                }
                if(index != -1) {
                    TileOven tileOven = (TileOven) world.getTileEntity(pos);
                    if (tileOven != null && tileOven.getToolItem(index).isEmpty()) {
                        ItemStack toolItem = heldItem.splitStack(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return true;
            }
        }
        if(facing == state.getValue(FACING)) {
            TileOven tileOven = (TileOven) world.getTileEntity(pos);
            if(tileOven != null) {
                if (player.isSneaking()) {
                    tileOven.getDoorAnimator().toggleForcedOpen();
                    return true;
                } else if (!heldItem.isEmpty() && tileOven.getDoorAnimator().isForcedOpen()) {
                    heldItem = ItemHandlerHelper.insertItemStacked(tileOven.getInputHandler(), heldItem, false);
                    if(!heldItem.isEmpty()) {
                        heldItem = ItemHandlerHelper.insertItemStacked(tileOven.getItemHandlerFuel(), heldItem, false);
                    }
                    player.setHeldItem(hand, heldItem);
                    return true;
                }
            }
        }
        if(!world.isRemote) {
            player.openGui(CookingForBlockheads.instance, GuiHandler.COOKING_OVEN, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileOven();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileOven tileEntity = (TileOven) world.getTileEntity(pos);
        if (tileEntity != null) {
            ItemUtils.dropItemHandlerItems(world, pos, tileEntity.getItemHandler());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileOven tileEntity = (TileOven) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.isBurning()) {
            EnumFacing facing = state.getValue(FACING);
            float x = (float) pos.getX() + 0.5f;
            float y = (float) pos.getY() + 0f + random.nextFloat() * 6f / 16f;
            float z = (float) pos.getZ() + 0.5f;
            float f3 = 0.52f;
            float f4 = random.nextFloat() * 0.6f - 0.3f;

            if (facing == EnumFacing.WEST) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (x - f3), (double) y, (double) (z + f4), 0, 0, 0);
            } else if (facing == EnumFacing.EAST) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (x + f3), (double) y, (double) (z + f4), 0, 0, 0);
            } else if (facing == EnumFacing.NORTH) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (x + f4), (double) y, (double) (z - f3), 0, 0, 0);
            } else if (facing == EnumFacing.SOUTH) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) (x + f4), (double) y, (double) (z + f3), 0, 0, 0);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

//    @Override
//    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
//        TileEntity tileEntity = world.getTileEntity(pos);
//        if(tileEntity instanceof TileOven) {
//            ((TileOven) tileEntity).setOvenColor(color);
//        }
//        return true;
//    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public void registerModels(ItemModelMesher mesher) {
//        super.registerModels(mesher);

//        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
//            @Override
//            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
//                if(world != null && pos != null) {
//                    TileEntity tileEntity = world.getTileEntity(pos);
//                    if (tileEntity instanceof TileOven) {
//                        return ((TileOven) tileEntity).getOvenColor().getMapColor().colorValue;
//                    }
//                }
//                return 0xFFFFFFFF;
//            }
//        }, this);
//    }
}
