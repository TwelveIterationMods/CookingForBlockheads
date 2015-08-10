package net.blay09.mods.cookingbook;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCookingTable extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    protected BlockCookingTable() {
        super(Material.wood);

        setUnlocalizedName("cookingbook:cookingtable");
        setTextureName("cookingbook:cooking_table_side");
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCookingTable();
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
