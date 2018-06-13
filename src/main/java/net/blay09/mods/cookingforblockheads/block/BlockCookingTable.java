package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ItemUtils;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCookingTable extends BlockKitchen {

    public BlockCookingTable() {
        super(Material.ROCK);

        setSoundType(SoundType.STONE);
        setHardness(2.5f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, COLOR);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCookingTable) {
            return state.withProperty(COLOR, ((TileCookingTable) tileEntity).getDyedColor());
        }

        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
        if (tileEntity == null) {
            return false;
        }

        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty()) {
            if (applyDye(world, pos, facing, player, heldItem)) {
                return true;
            }

            if (!tileEntity.hasNoFilterBook() && heldItem.getItem() == ModItems.noFilterBook) {
                if (!player.capabilities.isCreativeMode) {
                    heldItem.splitStack(1);
                }

                tileEntity.setHasNoFilterBook(true);
                return true;
            }
        }

        if (player.isSneaking()) {
            if (tileEntity.hasNoFilterBook()) {
                ItemStack noFilterBook = new ItemStack(ModItems.noFilterBook);
                if (!player.inventory.addItemStackToInventory(noFilterBook)) {
                    player.dropItem(noFilterBook, false);
                }

                tileEntity.setHasNoFilterBook(false);
                return true;
            }
        }

        if (!world.isRemote) {
            player.openGui(CookingForBlockheads.instance, GuiHandler.COOKING_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        // TODO move to getDrops in 1.13 if https://github.com/MinecraftForge/MinecraftForge/pull/4727 is merged
        TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
        if (tileEntity != null && tileEntity.hasNoFilterBook()) {
            ItemUtils.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.noFilterBook));
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isDyeable() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCookingTable();
    }

    @Override
    public String getIdentifier() {
        return "cooking_table";
    }

    @Nullable
    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileCookingTable.class;
    }

}
