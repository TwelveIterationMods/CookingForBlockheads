package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.client.render.ToolRackBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockToolRack extends BlockContainer {

    public BlockToolRack() {
        super(Material.iron);

        setBlockName("cookingbook:toolrack");
        setCreativeTab(CookingBook.creativeTab);
        setStepSound(soundTypeWood);
        setHardness(2.5f);
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
        switch(metadata) {
            case 2:
                setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
                break;
            case 3:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
                break;
            case 4:
                setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            case 5:
                setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
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
        return world.isSideSolid(x - 1, y, z, EAST ) ||
                world.isSideSolid(x + 1, y, z, WEST ) ||
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


}
