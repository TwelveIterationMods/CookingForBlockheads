package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.balyware.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

public class BlockOven extends BlockKitchen {

    private static final Random random = new Random();

    public BlockOven() {
        super(Material.iron);

        setRegistryName(CookingForBlockheads.MOD_ID, "oven");
        setUnlocalizedName(getRegistryName().toString());
        setStepSound(SoundType.METAL);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(side == EnumFacing.UP) {
            if(CookingRegistry.isToolItem(heldItem)) {
                EnumFacing facing = state.getValue(FACING);
                switch(facing) {
                    case NORTH: hitX = 1f - hitX; hitZ = 1f - hitZ; break;
//                    case SOUTH: hitX = hitX; hitZ = hitZ; break;
                    case WEST: hitZ = 1f - hitX; hitX = hitZ; break;
                    case EAST: hitZ = hitX; hitX = 1f - hitZ; break;
                }
                int index = -1;
                if(hitX < 0.5f && hitZ < 0.5f) {
                    index = 1;
                } else if(hitX >= 0.5f && hitZ < 0.5f) {
                    index = 0;
                } else if(hitX < 0.5f && hitZ >= 0.5f) {
                    index = 2;
                } else if(hitX >= 0.5f && hitZ >= 0.5f) {
                    index = 3;
                }
                if(index != -1) {
                    TileOven tileOven = (TileOven) world.getTileEntity(pos);
                    if (tileOven.getToolItem(index) == null) {
                        ItemStack toolItem = heldItem.splitStack(1);
                        tileOven.setToolItem(index, toolItem);
                    }
                }
                return true;
            }
        }
        if(side == state.getValue(FACING)) {
            TileOven tileOven = (TileOven) world.getTileEntity(pos);
            if(player.isSneaking()) {
                tileOven.getDoorAnimator().toggleForcedOpen();
                return true;
            } else if(heldItem != null && tileOven.getDoorAnimator().isForcedOpen()) {
                heldItem = ItemHandlerHelper.insertItemStacked(tileOven.getInputHandler(), heldItem, false);
                player.setHeldItem(hand, heldItem);
                return true;
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
            ItemUtils.dropContent(world, pos, tileEntity.getItemHandler());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        TileOven tileEntity = (TileOven) world.getTileEntity(pos);
        if (tileEntity.isBurning()) {
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
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(pos));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }
}
