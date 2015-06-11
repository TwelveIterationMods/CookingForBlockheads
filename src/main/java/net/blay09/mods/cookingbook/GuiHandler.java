package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.network.IGuiHandler;
import net.blay09.mods.cookingbook.client.GuiRecipeBook;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_ID_RECIPEBOOK = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new ContainerRecipeBook(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case GUI_ID_RECIPEBOOK:
                return new GuiRecipeBook(new ContainerRecipeBook(player.inventory));
        }
        return null;
    }

}
