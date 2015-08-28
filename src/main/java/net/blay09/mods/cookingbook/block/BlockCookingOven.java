package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.GuiHandler;
import net.blay09.mods.cookingbook.api.IKitchenStorageProvider;
import net.blay09.mods.cookingbook.client.render.FridgeBlockRenderer;
import net.blay09.mods.cookingbook.client.render.OvenBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockCookingOven extends BlockContainer {

    private final Random random = new Random();

    public BlockCookingOven() {
        super(Material.iron);

        setBlockName("cookingbook:cookingoven");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeMetal);
        setHardness(5f);
        setResistance(10f);
        setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.975f, 0.9375f);
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
        return OvenBlockRenderer.RENDER_ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.brick_block.getIcon(side, 0);
    }

//    @Override
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
//        if(hitY > 0.5f) {
//            int metadata = world.getBlockMetadata(x, y, z);
//            float hit = (metadata == 2 || metadata == 3) ? hitX : hitZ;
//            int hitSlot = hit > 0.5f ? 0 : 1;
//            TileEntityToolRack tileEntityToolRack = (TileEntityToolRack) world.getTileEntity(x, y, z);
//            if (tileEntityToolRack != null) {
//                if (player.getHeldItem() != null) {
//                    ItemStack oldToolItem = tileEntityToolRack.getStackInSlot(hitSlot);
//                    ItemStack toolItem = player.getHeldItem().splitStack(1);
//                    if (oldToolItem != null) {
//                        if (!player.inventory.addItemStackToInventory(oldToolItem)) {
//                            player.dropPlayerItemWithRandomChoice(oldToolItem, false);
//                        }
//                        tileEntityToolRack.setInventorySlotContents(hitSlot, toolItem);
//                    } else {
//                        tileEntityToolRack.setInventorySlotContents(hitSlot, toolItem);
//                    }
//                } else {
//                    ItemStack itemStack = tileEntityToolRack.getStackInSlot(hitSlot);
//                    if (itemStack != null) {
//                        tileEntityToolRack.setInventorySlotContents(hitSlot, null);
//                        player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
//                    }
//                }
//                return true;
//            }
//        }
//        return true;
//    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if(!world.isRemote) {
            player.openGui(CookingBook.instance, GuiHandler.GUI_ID_COOKINGOVEN, world, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCookingOven();
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
    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {
        TileEntityCookingOven tileEntity = (TileEntityCookingOven) world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            for (int i1 = 0; i1 < tileEntity.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileEntity.getStackInSlot(i1);
                if (itemstack != null) {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int j1 = this.random.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            world.func_147453_f(x, y, z, blockBroken); // updateNeighboursOfBlockChange
        }
        super.breakBlock(world, x, y, z, blockBroken, meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        TileEntityCookingOven tileEntity = (TileEntityCookingOven) world.getTileEntity(x, y, z);
        if (tileEntity.isBurning()) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float)x + 0.5F;
            float f1 = (float)y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }

}
