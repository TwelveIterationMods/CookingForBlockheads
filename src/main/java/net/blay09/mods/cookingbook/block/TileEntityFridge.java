package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenStorageProvider;
import net.blay09.mods.cookingbook.container.ContainerFridge;
import net.blay09.mods.cookingbook.container.InventoryFridge;
import net.blay09.mods.cookingbook.container.InventoryLargeFridge;
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

import java.util.List;
import java.util.Random;

public class TileEntityFridge extends TileEntity implements IInventory, IKitchenStorageProvider {

    private static final Random random = new Random();

    private InventoryFridge internalInventory;
    private IInventory sharedInventory;
    private EntityItem renderItem;
    private int fridgeColor;
    private boolean isFlipped;
    private float prevDoorAngle;
    private float doorAngle;
    private int numPlayersUsing;
    private int tickCounter;

    public TileEntityFridge() {
        internalInventory = new InventoryFridge();
        sharedInventory = internalInventory;
    }

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);

        renderItem = new EntityItem(world, 0, 0, 0);
        renderItem.hoverStart = 0f;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        tickCounter++;

        if (tickCounter == 1) {
            updateMultiblock();
        }

        fixBrokenContainerClosedCall();

        prevDoorAngle = doorAngle;
        if (numPlayersUsing > 0) {
            final float doorSpeed = 0.2f;
            doorAngle = Math.min(1f, doorAngle + doorSpeed);
        } else {
            final float doorSpeed = 0.1f;
            doorAngle = Math.max(0f, doorAngle - doorSpeed);
        }
    }

    private void fixBrokenContainerClosedCall() {
        // Because Mojang people thought it would be more sane to check chest watchers every few ticks instead of fixing the actual issue.
        if (!worldObj.isRemote && numPlayersUsing != 0 && (tickCounter + xCoord + yCoord + zCoord) % 200 == 0) {
            numPlayersUsing = 0;
            float range = 5.0F;
            List list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((float) xCoord - range, (float) yCoord - range, (float) zCoord - range, (float) xCoord + 1 + range, (float) yCoord + 1 + range, (float) zCoord + 1 + range));
            for (EntityPlayer entityPlayer : (List<EntityPlayer>) list) {
                if (entityPlayer.openContainer instanceof ContainerFridge) {
                    IInventory inventory = ((ContainerFridge) entityPlayer.openContainer).getFridgeInventory();
                    if (inventory == this || (inventory instanceof InventoryLargeFridge && ((InventoryLargeFridge) inventory).containsInventory(this))) {
                        numPlayersUsing++;
                    }
                }
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        if (id == 1) {
            numPlayersUsing = value;
            return true;
        } else if (id == 2) {
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

        internalInventory = new InventoryFridge();
        NBTTagList tagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
            internalInventory.setInventorySlotContents(itemCompound.getByte("Slot"), ItemStack.loadItemStackFromNBT(itemCompound));
        }
        fridgeColor = tagCompound.getByte("FridgeColor");
        isFlipped = tagCompound.getBoolean("IsFlipped");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < internalInventory.getSizeInventory(); i++) {
            ItemStack itemStack = internalInventory.getStackInSlot(i);
            if (itemStack != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                itemStack.writeToNBT(itemCompound);
                tagList.appendTag(itemCompound);
            }
        }
        tagCompound.setTag("Items", tagList);
        tagCompound.setByte("FridgeColor", (byte) fridgeColor);
        tagCompound.setBoolean("IsFlipped", isFlipped);
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

    public TileEntityFridge findNeighbourFridge() {
        if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) == CookingBook.blockFridge) {
            return (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        } else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == CookingBook.blockFridge) {
            return (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        }
        return null;
    }

    @Override
    public int getSizeInventory() {
        return sharedInventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return sharedInventory.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int amount) {
        return sharedInventory.decrStackSize(i, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return sharedInventory.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        sharedInventory.setInventorySlotContents(i, itemStack);
    }

    @Override
    public String getInventoryName() {
        return sharedInventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return sharedInventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return sharedInventory.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (hasWorldObj()) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    public float getDoorAngle() {
        return doorAngle;
    }

    public float getPrevDoorAngle() {
        return prevDoorAngle;
    }

    public void breakBlock() {
        for (int i = 0; i < internalInventory.getSizeInventory(); i++) {
            ItemStack itemStack = internalInventory.getStackInSlot(i);
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

    @Override
    public IInventory getInventory() {
        return this;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public void updateMultiblock() {
        TileEntityFridge bottomFridge;
        TileEntityFridge upperFridge;
        if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) == CookingBook.blockFridge) {
            bottomFridge = this;
            upperFridge = (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        } else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == CookingBook.blockFridge) {
            bottomFridge = (TileEntityFridge) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            upperFridge = this;
        } else {
            sharedInventory = internalInventory;
            return;
        }
        sharedInventory = new InventoryLargeFridge(bottomFridge.getInternalInventory(), upperFridge.getInternalInventory());
    }

    public InventoryFridge getInternalInventory() {
        return internalInventory;
    }
}