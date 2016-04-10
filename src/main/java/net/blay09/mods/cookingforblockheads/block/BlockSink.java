package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingConfig;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BlockSink extends BlockKitchen {

    public BlockSink() {
        super(Material.wood);

        setRegistryName(CookingForBlockheads.MOD_ID, "sink");
        setUnlocalizedName(getRegistryName().toString());
        setStepSound(SoundType.WOOD);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (FluidContainerRegistry.isEmptyContainer(heldItem)) {
            FluidStack fluidStack = null;
            int amount = FluidContainerRegistry.getContainerCapacity(new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME), heldItem);
            if(CookingConfig.sinkRequiresWater) {
                TileSink sink = (TileSink) world.getTileEntity(pos);
                if(sink.getWaterAmount() >= amount) {
                    fluidStack = sink.drain(null, amount, true);
                }
            } else {
                fluidStack = new FluidStack(FluidRegistry.WATER, amount);
            }
            if(fluidStack != null && fluidStack.amount >= amount) {
                ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(fluidStack, heldItem);
                if (filledContainer != null) {
                    if (heldItem.stackSize <= 1) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, filledContainer);
                    } else {
                        if (player.inventory.addItemStackToInventory(filledContainer)) {
                            heldItem.stackSize--;
                        }
                    }
                }
                spawnParticles(world, pos, state);
            }
            return true;
        } else if(FluidContainerRegistry.isFilledContainer(heldItem)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(heldItem);
            if(CookingConfig.sinkRequiresWater) {
                TileSink sink = (TileSink) world.getTileEntity(pos);
                sink.fill(null, fluidStack, true);
            }
            ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(heldItem);
            if(emptyContainer != null) {
                if(heldItem.stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, emptyContainer);
                } else {
                    if(player.inventory.addItemStackToInventory(emptyContainer)) {
                        heldItem.stackSize--;
                    }
                }
            }
            spawnParticles(world, pos, state);
            return true;
        } else {
            ItemStack resultStack = CookingRegistry.getSinkOutput(heldItem);
            if(resultStack != null) {
                NBTTagCompound tagCompound = heldItem.getTagCompound();
                ItemStack newItem = resultStack.copy();
                newItem.setTagCompound(tagCompound);
                if(heldItem.stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, newItem);
                } else {
                    if(player.inventory.addItemStackToInventory(newItem)) {
                        heldItem.stackSize--;
                    }
                }
                spawnParticles(world, pos, state);
                return true;
            } else {
                if(CookingConfig.sinkRequiresWater) {
                    TileSink sink = (TileSink) world.getTileEntity(pos);
                    if(sink.getWaterAmount() < FluidContainerRegistry.BUCKET_VOLUME) {
                        return true;
                    }
                }
                spawnParticles(world, pos, state);
            }
        }
        return true;
    }

    private void spawnParticles(World world, BlockPos pos, IBlockState state) {
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch(state.getValue(FACING)) {
            case NORTH: dripWaterZ = 0.25f; dripWaterX = -0.05f; break;
            case SOUTH: dripWaterX = 0.25f; break;
            case WEST: dripWaterX = 0.25f; dripWaterZ = 0.25f; break;
            case EAST: dripWaterZ = -0.05f; break;
        }
        float particleX = (float) pos.getX() + 0.5f;
        float particleY = (float) pos.getY() + 1.25f;
        float particleZ = (float) pos.getZ() + 0.5f;
        world.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for(int i = 0; i < 5; i++) {
            world.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileSink();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
