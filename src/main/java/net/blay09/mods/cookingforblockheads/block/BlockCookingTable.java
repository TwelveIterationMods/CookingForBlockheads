package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.blaycommon.ItemUtils;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class BlockCookingTable extends BlockKitchen {

    public BlockCookingTable() {
        super(Material.WOOD);

        setRegistryName(CookingForBlockheads.MOD_ID, "cooking_table");
        setUnlocalizedName(getRegistryNameString());
        setSoundType(SoundType.WOOD);
        setHardness(2.5f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(!heldItem.isEmpty()) {
            TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
            if(tileEntity != null) {
                if (!tileEntity.hasNoFilterBook() && heldItem.getItem() == ModItems.recipeBook && heldItem.getItemDamage() == 0) {
                    tileEntity.setNoFilterBook(heldItem.splitStack(1));
                    return true;
                }
            }
        } else if(player.isSneaking()) {
            TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
            if(tileEntity != null) {
                ItemStack noFilterBook = tileEntity.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.inventory.addItemStackToInventory(noFilterBook)) {
                        player.dropItem(noFilterBook, false);
                    }
                    tileEntity.setNoFilterBook(ItemStack.EMPTY);
                    return true;
                }
            }
        }
        if(!world.isRemote) {
            player.openGui(CookingForBlockheads.instance, GuiHandler.COOKING_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
        if(tileEntity != null) {
            ItemUtils.dropItem(world, pos, tileEntity.getNoFilterBook());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCookingTable();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
