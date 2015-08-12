package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCookingTable extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    public BlockCookingTable() {
        super(Material.wood);

        setUnlocalizedName("cookingbook:cookingtable");
        setTextureName("cookingbook:cooking_table_side");
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? iconTop : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? blockIcon : iconFront));
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconFront = iconRegister.registerIcon("cookingbook:cooking_table_front");
        iconTop = iconRegister.registerIcon("cookingbook:cooking_table_top");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if(!world.isRemote) {
            player.openGui(CookingBook.instance, GuiHandler.GUI_ID_COOKINGTABLE, world, x, y, z);
        }
        return true;
    }
}
