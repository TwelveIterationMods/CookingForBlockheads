package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.network.IGuiHandler;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_ID_RECIPEBOOK = 1;
    public static final int GUI_ID_RECIPEBOOK_WORLD = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new ContainerRecipeBook(player.inventory, player.inventory);
            case GUI_ID_RECIPEBOOK_WORLD:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof IInventory) {
                    return new ContainerRecipeBook(player.inventory, (IInventory) tileEntity);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player.inventory, player.inventory));
            case GUI_ID_RECIPEBOOK_WORLD:
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof IInventory) {
                    return new GuiRecipeBook(new ContainerRecipeBook(player.inventory, (IInventory) tileEntity));
                }
                break;
        }
        return null;
    }

}
