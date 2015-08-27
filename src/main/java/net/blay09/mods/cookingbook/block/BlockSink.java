package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.SinkHandlers;
import net.blay09.mods.cookingbook.api.IKitchenWaterProvider;
import net.blay09.mods.cookingbook.client.render.SinkBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class BlockSink extends BlockContainer implements IKitchenWaterProvider {

    private static final Random random = new Random();

    public BlockSink() {
        super(Material.iron);

        setBlockName("cookingbook:sink");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeMetal);
        setHardness(5f);
        setResistance(10f);
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        super.onBlockAdded(worldIn, x, y, z);
        findOrientation(worldIn, x, y, z);
    }

    private void findOrientation(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte side = 3;
            if (block.isOpaqueCube() && !block1.isOpaqueCube()) {
                side = 3;
            }
            if (block1.isOpaqueCube() && !block.isOpaqueCube()) {
                side = 2;
            }
            if (block2.isOpaqueCube() && !block3.isOpaqueCube()) {
                side = 5;
            }
            if (block3.isOpaqueCube() && !block2.isOpaqueCube()) {
                side = 4;
            }
            world.setBlockMetadataWithNotify(x, y, z, side, 2);
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return SinkBlockRenderer.RENDER_ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.log.getIcon(side, 1);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (FluidContainerRegistry.isEmptyContainer(player.getHeldItem())) {
            ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(FluidRegistry.getFluidStack("water", 1000), player.getHeldItem());
            if(filledContainer != null) {
                if(player.getHeldItem().stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filledContainer);
                } else {
                    if(player.inventory.addItemStackToInventory(filledContainer)) {
                        player.getHeldItem().stackSize--;
                    }
                }
            }
            spawnParticles(world, x, y, z);
            return true;
        } else if(FluidContainerRegistry.isFilledContainer(player.getHeldItem())) {
            ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(player.getHeldItem());
            if(emptyContainer != null) {
                if(player.getHeldItem().stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, emptyContainer);
                } else {
                    if(player.inventory.addItemStackToInventory(emptyContainer)) {
                        player.getHeldItem().stackSize--;
                    }
                }
            }
            spawnParticles(world, x, y, z);
            return true;
        } else {
            ItemStack resultStack = SinkHandlers.getSinkOutput(player.getHeldItem());
            if(resultStack != null) {
                ItemStack oldItem = player.getHeldItem();
                NBTTagCompound tagCompound = oldItem.getTagCompound();
                ItemStack newItem = resultStack.copy();
                newItem.setTagCompound(tagCompound);
                if(oldItem.stackSize <= 1) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, newItem);
                } else {
                    if(player.inventory.addItemStackToInventory(newItem)) {
                        oldItem.stackSize--;
                    }
                }
                spawnParticles(world, x, y, z);
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    public void spawnParticles(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        float dripWaterX = 0f;
        float dripWaterZ = 0f;
        switch(metadata) {
            case 2: dripWaterZ = 0.25f; dripWaterX = -0.05f; break;
            case 3: dripWaterX = 0.25f; break;
            case 4: dripWaterX = 0.25f; dripWaterZ = 0.25f; break;
            case 5: dripWaterZ = -0.05f; break;
        }
        float particleX = (float) x + 0.5f;
        float particleY = (float) y + 1.25f;
        float particleZ = (float) z + 0.5f;
        world.spawnParticle("dripWater", (double) particleX + dripWaterX, (double) particleY - 0.45f, (double) particleZ + dripWaterZ, 0, 0, 0);
        for(int i = 0; i < 5; i++) {
            world.spawnParticle("splash", (double) particleX + Math.random() - 0.5f, (double) particleY + Math.random() - 0.5f, (double) particleZ + Math.random() - 0.5f, 0, 0, 0);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack itemStack) {
        int l = MathHelper.floor_double((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntitySink();
    }

    @Override
    public boolean consumeWater(World world, int x, int y, int z) {
        return true;
    }
}
