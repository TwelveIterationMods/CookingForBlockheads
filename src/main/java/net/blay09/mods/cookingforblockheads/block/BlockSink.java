package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockSink extends BlockKitchen {

    public static final String name = "sink";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public BlockSink() {
        super(Material.ROCK);

        setUnlocalizedName(registryName.toString());
        setSoundType(SoundType.STONE);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, COLOR, FLIPPED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileSink) {
            return state.withProperty(COLOR, ((TileSink) tileEntity).getDyedColor());
        }
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, meta, placer);
        return state.withProperty(FLIPPED, shouldBePlacedFlipped(pos, state.getValue(FACING), placer));
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

        ItemStack resultStack = CookingRegistry.getSinkOutput(heldItem);
        if (!resultStack.isEmpty()) {
            NBTTagCompound tagCompound = heldItem.getTagCompound();
            ItemStack newItem = resultStack.copy();
            if (tagCompound != null) {
                newItem.setTagCompound(tagCompound);
            }
            if (heldItem.getCount() <= 1) {
                player.setHeldItem(hand, newItem);
            } else {
                if (player.inventory.addItemStackToInventory(newItem)) {
                    heldItem.shrink(1);
                }
            }
            spawnParticles(world, pos, state);
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, world.rand.nextFloat() + 0.5f);
            return true;
        } else {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null) {
                IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                if (fluidHandler == null) {
                    spawnParticles(world, pos, state);
                } else {
                    if (!FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) {
                        // Special case for bottles, they can hold 1/3 of a bucket
                        if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                            FluidStack simulated = fluidHandler.drain(333, false);
                            if (simulated != null && simulated.amount == 333) {
                                fluidHandler.drain(333, true);
                                if (player.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER))) {
                                    heldItem.shrink(1);
                                }
                            } else {
                                spawnParticles(world, pos, state);
                            }
                        } else {
                            spawnParticles(world, pos, state);
                        }
                    }
                }
                return !heldItem.isEmpty() && !(heldItem.getItem() instanceof ItemBlock);
            }
        }

        return true;
    }

    private void spawnParticles(World world, BlockPos pos, IBlockState state) {
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch (state.getValue(FACING)) {
            case NORTH:
                dripWaterZ = 0.25f;
                dripWaterX = -0.05f;
                break;
            case SOUTH:
                dripWaterX = 0.25f;
                break;
            case WEST:
                dripWaterX = 0.25f;
                dripWaterZ = 0.25f;
                break;
            case EAST:
                dripWaterZ = -0.05f;
                break;
        }

        float particleX = (float) pos.getX() + 0.5f;
        float particleY = (float) pos.getY() + 1.25f;
        float particleZ = (float) pos.getZ() + 0.5f;
        world.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for (int i = 0; i < 5; i++) {
            world.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
        }

        world.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 0.1f, world.rand.nextFloat() + 0.5f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileSink();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);

        for (String s : I18n.format("tooltip." + registryName + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

    @Override
    public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileSink) {
            TileSink tileSink = (TileSink) tileEntity;
            if (tileSink.getDyedColor() == color) {
                return false;
            }

            tileSink.setDyedColor(color);
        }
        return true;
    }

}
