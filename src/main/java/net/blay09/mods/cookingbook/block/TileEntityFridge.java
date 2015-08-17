package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

public class TileEntityFridge extends TileEntity implements IInventory {

    private ItemStack[] inventory = new ItemStack[27];
    private TileEntityFridge neighbourFridge;
    private int fridgeColor;

    public void findNeighbourFridge() {
        if(worldObj.getBlock(xCoord, yCoord + 1, zCoord) == CookingBook.blockFridge) {
            neighbourFridge = (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        } else if(worldObj.getBlock(xCoord, yCoord - 1, zCoord) == CookingBook.blockFridge) {
            neighbourFridge = (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        } else {
            neighbourFridge = null;
        }
    }

    @Override
    public int getSizeInventory() {
        return inventory.length + (neighbourFridge != null ? neighbourFridge.getSizeInventory() : 0);
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(i < inventory.length) {
            return inventory[i];
        } else if(neighbourFridge != null) {
            return neighbourFridge.getStackInSlot(i - inventory.length);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if(i < inventory.length) {
            if(inventory[i] != null) {
                ItemStack itemStack;
                if (inventory[i].stackSize <= count) {
                    itemStack = inventory[i];
                    inventory[i] = null;
                    return itemStack;
                } else {
                    itemStack = inventory[i].splitStack(count);
                    if (inventory[i].stackSize == 0) {
                        inventory[i] = null;
                    }
                    return itemStack;
                }
            }
        } else if(neighbourFridge != null) {
            return neighbourFridge.decrStackSize(i - inventory.length, count);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (inventory[i] != null) {
            ItemStack itemstack = inventory[i];
            inventory[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if(i < inventory.length) {
            inventory[i] = itemStack;
        } else if(neighbourFridge != null) {
            neighbourFridge.setInventorySlotContents(i - inventory.length, itemStack);
        }
    }

    @Override
    public String getInventoryName() {
        return "container.cookingbook:fridge";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    public boolean isLargeFridge() {
        return neighbourFridge != null;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 2, zCoord + 1);
    }

    public void setFridgeColor(int fridgeColor) {
        this.fridgeColor = fridgeColor;
        markDirty();
    }

    public int getFridgeColor() {
        return fridgeColor;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
            inventory[itemCompound.getByte("Slot")] = ItemStack.loadItemStackFromNBT(itemCompound);
        }
        fridgeColor = tagCompound.getByte("FridgeColor");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList tagList = new NBTTagList();
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemCompound);
            }
        }
        tagCompound.setTag("Items", tagList);
        tagCompound.setByte("FridgeColor", (byte) fridgeColor);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tagCompound = pkt.func_148857_g();
        readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }

    public TileEntityFridge getNeighbourFridge() {
        return neighbourFridge;
    }
}
