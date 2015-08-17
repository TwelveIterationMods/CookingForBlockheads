package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.network.IGuiHandler;
import net.blay09.mods.cookingbook.block.TileEntityCookingOven;
import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.blay09.mods.cookingbook.client.GuiCookingOven;
import net.blay09.mods.cookingbook.client.GuiFridge;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
import net.blay09.mods.cookingbook.container.ContainerCookingOven;
import net.blay09.mods.cookingbook.container.ContainerFridge;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_ID_RECIPEBOOK = 1;
    public static final int GUI_ID_CRAFTBOOK = 2;
    public static final int GUI_ID_NOFILTERBOOK = 3;
    public static final int GUI_ID_COOKINGTABLE = 4;
    public static final int GUI_ID_COOKINGOVEN = 5;
    public static final int GUI_ID_FRIDGE = 6;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new ContainerRecipeBook(player, false, false, false).setSourceInventories(player.inventory);
            case GUI_ID_CRAFTBOOK:
                if(player.getHeldItem() != null && player.getHeldItem().getItemDamage() == 1) {
                    return new ContainerRecipeBook(player, CookingBook.enableCraftingBook, false, false).setSourceInventories(player.inventory);
                }
                break;
            case GUI_ID_NOFILTERBOOK:
                if(player.getHeldItem() != null && player.getHeldItem().getItemDamage() == 3) {
                    return new ContainerRecipeBook(player, false, false, false).setSourceInventories(player.inventory).setNoFilter();
                }
                break;
            case GUI_ID_COOKINGTABLE:
                if(world.getBlock(x, y, z) == CookingBook.blockCookingTable) {
                    List<IInventory> inventories = new ArrayList<IInventory>();
                    boolean hasOven = false;
                    for(ForgeDirection direction : ForgeDirection.values()) {
                        if(direction == ForgeDirection.UNKNOWN) {
                            continue;
                        }
                        TileEntity tileEntity = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
                        if(tileEntity instanceof IInventory) {
                            inventories.add((IInventory) tileEntity);
                        }
                        if(tileEntity != null && tileEntity.getClass() == TileEntityCookingOven.class) {
                            hasOven = true;
                        }
                    }
                    return new ContainerRecipeBook(player, true, hasOven, false).setSourceInventories(inventories.toArray(new IInventory[inventories.size()])).setTilePosition(world, x, y, z);
                }
                break;
            case GUI_ID_COOKINGOVEN:
                if(world.getBlock(x, y, z) == CookingBook.blockCookingOven) {
                    return new ContainerCookingOven(player.inventory, (TileEntityCookingOven) world.getTileEntity(x, y, z));
                }
                break;
            case GUI_ID_FRIDGE:
                if(world.getBlock(x, y, z) == CookingBook.blockFridge) {
                    return new ContainerFridge(player.inventory, (TileEntityFridge) world.getTileEntity(x, y, z));
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, false, false, true));
            case GUI_ID_CRAFTBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, CookingBook.enableCraftingBook, false, true));
            case GUI_ID_NOFILTERBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, true, false, true));
            case GUI_ID_COOKINGTABLE:
                return new GuiRecipeBook(new ContainerRecipeBook(player, true, true, true));
            case GUI_ID_COOKINGOVEN:
                return new GuiCookingOven(player.inventory, (TileEntityCookingOven) world.getTileEntity(x, y, z));
            case GUI_ID_FRIDGE:
                return new GuiFridge(player.inventory, (TileEntityFridge) world.getTileEntity(x, y, z));
        }
        return null;
    }

}
