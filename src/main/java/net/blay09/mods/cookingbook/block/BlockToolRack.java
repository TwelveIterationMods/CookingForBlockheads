package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.client.render.ToolRackBlockRenderer;
import net.blay09.mods.cookingbook.item.ItemBlockCookingOven;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockToolRack extends BlockContainer {

    private static final Random random = new Random();

    public BlockToolRack() {
        super(Material.wood);

        setBlockName("cookingbook:toolrack");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeWood);
        setHardness(2.5f);
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.planks.getIcon(side, 0);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityToolRack();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
        float f = 0.125F;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        switch (metadata) {
            case 2:
                setBlockBounds(0f, 0.25f, 1f - f, 1f, 1f, 1f);
                break;
            case 3:
                setBlockBounds(0f, 0.25f, 0f, 1f, 1f, f);
                break;
            case 4:
                setBlockBounds(1f - f, 0.25f, 0f, 1f, 1f, 1f);
                break;
            case 5:
                setBlockBounds(0f, 0.25f, 0f, f, 1f, 1f);
                break;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ToolRackBlockRenderer.RENDER_ID;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isSideSolid(x - 1, y, z, EAST) ||
                world.isSideSolid(x + 1, y, z, WEST) ||
                world.isSideSolid(x, y, z - 1, SOUTH) ||
                world.isSideSolid(x, y, z + 1, NORTH);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        int newMetadata = metadata;
        if ((metadata == 0 || side == 2) && world.isSideSolid(x, y, z + 1, NORTH)) {
            newMetadata = 2;
        }
        if ((newMetadata == 0 || side == 3) && world.isSideSolid(x, y, z - 1, SOUTH)) {
            newMetadata = 3;
        }
        if ((newMetadata == 0 || side == 4) && world.isSideSolid(x + 1, y, z, WEST)) {
            newMetadata = 4;
        }
        if ((newMetadata == 0 || side == 5) && world.isSideSolid(x - 1, y, z, EAST)) {
            newMetadata = 5;
        }
        return newMetadata;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock) {
            return true;
        }
        if(hitY > 0.25f) {
            int metadata = world.getBlockMetadata(x, y, z);
            float hit = hitX;
            switch(metadata) {
                case 2: hit = hitX; break;
                case 3: hit = 1f - hitX; break;
                case 4: hit = 1f - hitZ; break;
                case 5: hit = hitZ; break;
            }
            int hitSlot = hit > 0.5f ? 0 : 1;
            TileEntityToolRack tileEntityToolRack = (TileEntityToolRack) world.getTileEntity(x, y, z);
            if (tileEntityToolRack != null) {
                if (player.getHeldItem() != null) {

                    ItemStack oldToolItem = tileEntityToolRack.getStackInSlot(hitSlot);
                    ItemStack toolItem = player.getHeldItem().splitStack(1);
                    if (oldToolItem != null) {
                        if (!player.inventory.addItemStackToInventory(oldToolItem)) {
                            player.dropPlayerItemWithRandomChoice(oldToolItem, false);
                        }
                        tileEntityToolRack.setInventorySlotContents(hitSlot, toolItem);
                    } else {
                        tileEntityToolRack.setInventorySlotContents(hitSlot, toolItem);
                    }
                } else {
                    ItemStack itemStack = tileEntityToolRack.getStackInSlot(hitSlot);
                    if (itemStack != null) {
                        tileEntityToolRack.setInventorySlotContents(hitSlot, null);
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntityToolRack tileEntity = (TileEntityToolRack) world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    float offsetX = random.nextFloat() * 0.8F + 0.1F;
                    float offsetY = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem;

                    for (float offsetZ = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld(entityItem)) {
                        int stackSize = random.nextInt(21) + 10;

                        if (stackSize > itemStack.stackSize) {
                            stackSize = itemStack.stackSize;
                        }

                        itemStack.stackSize -= stackSize;
                        entityItem = new EntityItem(world, (double) ((float) x + offsetX), (double) ((float) y + offsetY), (double) ((float) z + offsetZ), new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage()));
                        float f3 = 0.05F;
                        entityItem.motionX = (double) ((float) random.nextGaussian() * f3);
                        entityItem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
                        entityItem.motionZ = (double) ((float) random.nextGaussian() * f3);

                        if (itemStack.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                        }
                    }
                }
            }

            super.breakBlock(world, x, y, z, block, metadata);
        }
    }

}