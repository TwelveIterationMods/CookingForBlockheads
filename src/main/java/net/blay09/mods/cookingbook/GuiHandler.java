package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.network.IGuiHandler;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
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
    public static final int GUI_ID_SMELTBOOK = 3;
    public static final int GUI_ID_NOFILTERBOOK = 4;
    public static final int GUI_ID_COOKINGTABLE = 5;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new ContainerRecipeBook(player, false, false).setSourceInventories(player.inventory);
            case GUI_ID_CRAFTBOOK:
                if(player.getHeldItem() != null && player.getHeldItem().getMetadata() == 1) {
                    return new ContainerRecipeBook(player, CookingBook.enableCraftingBook, false).setSourceInventories(player.inventory);
                }
                break;
            case GUI_ID_NOFILTERBOOK:
                if(player.getHeldItem() != null && player.getHeldItem().getMetadata() == 3) {
                    return new ContainerRecipeBook(player, false, false).setSourceInventories(player.inventory).setNoFilter();
                }
                break;
            case GUI_ID_COOKINGTABLE:
                if(world.getBlock(x, y, z) == CookingBook.blockCookingTable) {
                    List<IInventory> inventories = new ArrayList<IInventory>();
                    for(ForgeDirection direction : ForgeDirection.values()) {
                        if(direction == ForgeDirection.UNKNOWN) {
                            continue;
                        }
                        TileEntity tileEntity = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
                        if(tileEntity instanceof IInventory) {
                            inventories.add((IInventory) tileEntity);
                        }
                    }
                    return new ContainerRecipeBook(player, true, false).setSourceInventories(inventories.toArray(new IInventory[inventories.size()]));
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, false, true));
            case GUI_ID_CRAFTBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, CookingBook.enableCraftingBook, true));
            case GUI_ID_NOFILTERBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player, true, true));
            case GUI_ID_COOKINGTABLE:
                return new GuiRecipeBook(new ContainerRecipeBook(player, true, true));
        }
        return null;
    }

}
