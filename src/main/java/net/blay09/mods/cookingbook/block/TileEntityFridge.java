package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TileEntityFridge extends TileEntity implements IInventory {

    private static final Random random = new Random();

    private ItemStack[] inventory = new ItemStack[27];
    private TileEntityFridge neighbourFridge;
    private EntityItem renderItem;
    private int fridgeColor;
    private float prevDoorAngle;
    private float doorAngle;
    private int numPlayersUsing;

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);

        renderItem = new EntityItem(world, 0, 0, 0);
        renderItem.hoverStart = 0f;
    }

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
        return inventory.length + (neighbourFridge != null ? neighbourFridge.inventory.length : 0);
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
                    markDirty();
                    return itemStack;
                } else {
                    itemStack = inventory[i].splitStack(count);
                    if (inventory[i].stackSize == 0) {
                        inventory[i] = null;
                    }
                    markDirty();
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
            markDirty();
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
    public void updateEntity() {
        super.updateEntity();

        prevDoorAngle = doorAngle;
        if(numPlayersUsing > 0) {
            final float doorSpeed = 0.2f;
            doorAngle = Math.min(1f, doorAngle + doorSpeed);
        } else {
            final float doorSpeed = 0.1f;
            doorAngle = Math.max(0f, doorAngle - doorSpeed);
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
    public boolean receiveClientEvent(int id, int value) {
        if(id == 1) {
            numPlayersUsing = value;
            return true;
        } else if(id == 2) {
            fridgeColor = value;
            return true;
        }
        return super.receiveClientEvent(id, value);
    }

    @Override
    public void openInventory() {
        numPlayersUsing++;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
    }

    @Override
    public void closeInventory() {
        numPlayersUsing--;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
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
        return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
    }

    public void setFridgeColor(int fridgeColor) {
        this.fridgeColor = fridgeColor;
        markDirty();
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 2, fridgeColor);
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
                tagList.appendTag(itemCompound);
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

    public float getDoorAngle() {
        return doorAngle;
    }

    public float getPrevDoorAngle() {
        return prevDoorAngle;
    }

    public void breakBlock() {
        for (ItemStack itemStack : inventory) {
            if (itemStack != null) {
                float offsetX = random.nextFloat() * 0.8f + 0.1f;
                float offsetY = random.nextFloat() * 0.8f + 0.1f;
                EntityItem entityItem;
                for (float offsetZ = random.nextFloat() * 0.8f + 0.1f; itemStack.stackSize > 0; worldObj.spawnEntityInWorld(entityItem)) {
                    int stackSize = random.nextInt(21) + 10;

                    if (stackSize > itemStack.stackSize) {
                        stackSize = itemStack.stackSize;
                    }

                    itemStack.stackSize -= stackSize;
                    entityItem = new EntityItem(worldObj, (double) ((float) xCoord + offsetX), (double) ((float) yCoord + offsetY), (double) ((float) zCoord + offsetZ), new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage()));
                    float f3 = 0.05F;
                    entityItem.motionX = (double) ((float) random.nextGaussian() * f3);
                    entityItem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
                    entityItem.motionZ = (double) ((float) random.nextGaussian() * f3);

                    if (itemStack.hasTagCompound()) {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                    }
                }
            }
        }
    }

    public EntityItem getRenderItem() {
        return renderItem;
    }
}
