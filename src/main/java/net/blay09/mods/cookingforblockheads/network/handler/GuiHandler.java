package net.blay09.mods.cookingforblockheads.network.handler;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.blay09.mods.cookingforblockheads.client.gui.GuiOven;
import net.blay09.mods.cookingforblockheads.client.gui.GuiFridge;
import net.blay09.mods.cookingforblockheads.client.gui.GuiRecipeBook;
import net.blay09.mods.cookingforblockheads.container.ContainerOven;
import net.blay09.mods.cookingforblockheads.container.ContainerFridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int ITEM_RECIPE_BOOK = 1;
    public static final int COOKING_TABLE = 2;
    public static final int COOKING_OVEN = 3;
    public static final int FRIDGE = 4;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == ITEM_RECIPE_BOOK) { // x: EnumHand
            if(x < 0 || x >= EnumHand.values().length) {
                return null;
            }
            ItemStack heldItem = player.getHeldItem(EnumHand.values()[x]);
            switch(heldItem.getItemDamage()) {
                case 0:
                    return new ContainerRecipeBook(player).setNoFilter();
                case 1:
                    return new ContainerRecipeBook(player);
                case 2:
                    return new ContainerRecipeBook(player).allowCrafting();
            }
        } else {
            BlockPos pos = new BlockPos(x, y, z);
            switch(id) {
                case COOKING_TABLE:
                    if(world.getBlockState(pos).getBlock() == ModBlocks.cookingTable) {
                        TileCookingTable tileEntity = (TileCookingTable) world.getTileEntity(pos);
                        if(tileEntity.hasNoFilterBook()) {
                            return new ContainerRecipeBook(player).setNoFilter().allowCrafting().setKitchenMultiBlock(new KitchenMultiBlock(world, pos));
                        } else {
                            return new ContainerRecipeBook(player).allowCrafting().setKitchenMultiBlock(new KitchenMultiBlock(world, pos));
                        }
                    }
                    break;
                case COOKING_OVEN:
                    if(world.getBlockState(pos).getBlock() == ModBlocks.oven) {
                        return new ContainerOven(player, (TileOven) world.getTileEntity(pos));
                    }
                    break;
                case FRIDGE:
                    if(world.getBlockState(pos).getBlock() == ModBlocks.fridge) {
                        return new ContainerFridge(player, (TileFridge) world.getTileEntity(pos));
                    }
                    break;
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if(id == ITEM_RECIPE_BOOK) { // x: EnumHand
            ItemStack heldItem = player.getHeldItem(EnumHand.values()[x]);
            switch(heldItem.getItemDamage()) {
                case 0:
                    return new GuiRecipeBook(new ContainerRecipeBook(player).setNoFilter());
                case 1:
                    return new GuiRecipeBook(new ContainerRecipeBook(player));
                case 2:
                    return new GuiRecipeBook(new ContainerRecipeBook(player).allowCrafting());
            }
        } else {
            BlockPos pos = new BlockPos(x, y, z);
            switch(id) {
                case COOKING_TABLE:
                    return new GuiRecipeBook(new ContainerRecipeBook(player).allowCrafting());
                case COOKING_OVEN:
                    return new GuiOven(player, (TileOven) world.getTileEntity(pos));
                case FRIDGE:
                    return new GuiFridge(player, (TileFridge) world.getTileEntity(pos));
            }
        }
        return null;
    }

}
