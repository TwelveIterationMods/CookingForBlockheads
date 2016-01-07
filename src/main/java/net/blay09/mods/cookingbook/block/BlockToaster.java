package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingForBlockheads;
import net.blay09.mods.cookingbook.client.render.ToasterBlockRenderer;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockToaster extends BlockContainer {

    public BlockToaster() {
        super(Material.iron);

        setBlockName(CookingForBlockheads.MOD_ID + ":toaster");
        setCreativeTab(CookingForBlockheads.creativeTab);
        setStepSound(soundTypeWood);
        setHardness(5f);
        setResistance(10f);
        setBlockBounds(0.275f, 0f, 0.275f, 0.725f, 0.4f, 0.725f);
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
        return ToasterBlockRenderer.RENDER_ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.iron_block.getIcon(side, 1);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityToaster tileEntity = (TileEntityToaster) world.getTileEntity(x, y, z);
        ItemStack heldItem = player.getHeldItem();
        if(heldItem == null) {
//            if(!tileEntity.isActive()) {
                tileEntity.setActive(!tileEntity.isActive());
//            }
        } else {
            ItemStack output = CookingRegistry.getToastOutput(heldItem);
            if(output != null) {
                for(int i = 0; i < tileEntity.getSizeInventory(); i++) {
                    if(tileEntity.getStackInSlot(i) == null) {
                        tileEntity.setInventorySlotContents(i, heldItem.splitStack(1));
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
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
        return new TileEntityToaster();
    }

}
