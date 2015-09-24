package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.GuiHandler;
import net.blay09.mods.cookingbook.client.render.CookingTableBlockRenderer;
import net.blay09.mods.cookingbook.client.render.SinkBlockRenderer;
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

public class BlockCookingTable extends BlockContainer {

    public BlockCookingTable() {
        super(Material.wood);

        setBlockName("cookingbook:cookingtable");
        setBlockTextureName("cookingbook:cooking_table_side");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeWood);
        setHardness(2.5f);
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
        return CookingTableBlockRenderer.RENDER_ID;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return Blocks.log.getIcon(side, 1);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        ItemStack heldItem = player.getHeldItem();
        if(heldItem != null) {
            TileEntityCookingTable tileEntity = (TileEntityCookingTable) world.getTileEntity(x, y, z);
            if(!tileEntity.hasNoFilterBook() && heldItem.getItem() == CookingBook.itemRecipeBook && heldItem.getItemDamage() == 3) {
                tileEntity.setNoFilterBook(heldItem.splitStack(1));
                return true;
            }
        } else if(player.isSneaking()) {
            TileEntityCookingTable tileEntity = (TileEntityCookingTable) world.getTileEntity(x, y, z);
            ItemStack noFilterBook = tileEntity.getNoFilterBook();
            if(noFilterBook != null) {
                if(!player.inventory.addItemStackToInventory(noFilterBook)) {
                    player.dropPlayerItemWithRandomChoice(noFilterBook, false);
                }
                tileEntity.setNoFilterBook(null);
                return true;
            }
        }
        if(!world.isRemote) {
            player.openGui(CookingBook.instance, GuiHandler.GUI_ID_COOKINGTABLE, world, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityCookingTable();
    }
}
