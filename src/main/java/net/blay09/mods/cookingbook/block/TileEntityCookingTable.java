package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.api.kitchen.IMultiblockKitchen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityCookingTable extends TileEntity implements IMultiblockKitchen {

    private EntityItem renderItem;
    private ItemStack noFilterBook;

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);

        renderItem = new EntityItem(world, 0, 0, 0);
        renderItem.hoverStart = 0f;
        if(noFilterBook != null) {
            renderItem.setEntityItemStack(noFilterBook);
        }
    }

    public EntityItem getRenderItem() {
        return renderItem;
    }

    public boolean hasNoFilterBook() {
        return noFilterBook != null;
    }

    public ItemStack getNoFilterBook() {
        return noFilterBook;
    }

    public void setNoFilterBook(ItemStack noFilterBook) {
        this.noFilterBook = noFilterBook;
        if(renderItem != null) {
            renderItem.setEntityItemStack(noFilterBook);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound itemCompound = new NBTTagCompound();
        if(noFilterBook != null) {
            noFilterBook.writeToNBT(itemCompound);
        }
        tagCompound.setTag("NoFilterBook", itemCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if(tagCompound.hasKey("NoFilterBook")) {
            setNoFilterBook(ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("NoFilterBook")));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);

        readFromNBT(pkt.func_148857_g());
    }
}
