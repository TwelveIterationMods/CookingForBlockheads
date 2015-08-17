package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.GuiHandler;
import net.blay09.mods.cookingbook.client.render.FridgeBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFridge extends BlockContainer {

    public BlockFridge() {
        super(Material.iron);

        setBlockName("cookingbook:fridge");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeMetal);
        setHardness(5f);
        setResistance(10f);
        setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.975f, 0.9375f);
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.iron_block.getIcon(side, 0);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
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
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityFridge();
    }

    @Override
    public int getRenderType() {
        return FridgeBlockRenderer.RENDER_ID;
    }

    @Override
    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
        TileEntityFridge fridge = (TileEntityFridge) world.getTileEntity(x, y, z);
        fridge.setFridgeColor(colour);
        if(fridge.getNeighbourFridge() != null) {
            fridge.getNeighbourFridge().setFridgeColor(colour);
        }
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(player.getHeldItem() != null && player.getHeldItem().getItem() == Items.dye) {
            int dye = BlockColored.func_150032_b(player.getHeldItem().getItemDamage());
            TileEntityFridge fridge = (TileEntityFridge) world.getTileEntity(x, y, z);
            fridge.setFridgeColor(dye);
            if(fridge.getNeighbourFridge() != null) {
                fridge.getNeighbourFridge().setFridgeColor(dye);
            }
            player.getHeldItem().stackSize--;
            return true;
        }
        if(!world.isRemote) {
            player.openGui(CookingBook.instance, GuiHandler.GUI_ID_FRIDGE, world, x, y, z);
        }
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        if(world.getBlock(x, y - 1, z) == CookingBook.blockFridge && world.getBlock(x, y - 2, z) == CookingBook.blockFridge) {
            return false;
        }
        if(world.getBlock(x, y + 1, z) == CookingBook.blockFridge && world.getBlock(x, y + 2, z) == CookingBook.blockFridge) {
            return false;
        }
        return super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        ((TileEntityFridge) world.getTileEntity(x, y, z)).findNeighbourFridge();
        findOrientation(world, x, y, z);
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
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
        ((TileEntityFridge) world.getTileEntity(x, y, z)).findNeighbourFridge();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbourBlock) {
        super.onNeighborBlockChange(world, x, y, z, neighbourBlock);
        ((TileEntityFridge) world.getTileEntity(x, y, z)).findNeighbourFridge();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntityFridge tileEntityFridge = (TileEntityFridge) world.getTileEntity(x, y, z);
        if(tileEntityFridge != null) {
            tileEntityFridge.breakBlock();
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }
}
