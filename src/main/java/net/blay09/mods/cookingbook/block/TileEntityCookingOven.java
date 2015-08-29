package net.blay09.mods.cookingbook.block;

import net.blay09.mods.cookingbook.api.kitchen.IKitchenSmeltingProvider;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenStorageProvider;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.ArrayUtils;

public class TileEntityCookingOven extends TileEntity implements ISidedInventory, IKitchenSmeltingProvider, IKitchenStorageProvider {

    public static class OvenInventory implements IInventory {
        private final IInventory inventory;

        public OvenInventory(IInventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public int getSizeInventory() {
            return 7;
        }

        @Override
        public ItemStack getStackInSlot(int i) {
            if(i >= 3) {
                return inventory.getStackInSlot(16 + i - 3);
            }
            return inventory.getStackInSlot(i + 4);
        }

        @Override
        public ItemStack decrStackSize(int i, int amount) {
            if(i >= 3) {
                return inventory.decrStackSize(16 + i - 3, amount);
            }
            return inventory.decrStackSize(i + 4, amount);
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int i) {
            if(i >= 3) {
                return inventory.getStackInSlotOnClosing(16 + i - 3);
            }
            return inventory.getStackInSlotOnClosing(i + 4);
        }

        @Override
        public void setInventorySlotContents(int i, ItemStack itemStack) {
            if(i >= 3) {
                inventory.setInventorySlotContents(16 + i - 3, itemStack);
            } else {
                inventory.setInventorySlotContents(i + 4, itemStack);
            }
        }

        @Override
        public String getInventoryName() {
            return inventory.getInventoryName();
        }

        @Override
        public boolean hasCustomInventoryName() {
            return inventory.hasCustomInventoryName();
        }

        @Override
        public int getInventoryStackLimit() {
            return inventory.getInventoryStackLimit();
        }

        @Override
        public void markDirty() {
            inventory.markDirty();
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer player) {
            return inventory.isUseableByPlayer(player);
        }

        @Override
        public void openInventory() {
            inventory.openInventory();
        }

        @Override
        public void closeInventory() {
            inventory.closeInventory();
        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemStack) {
            if(i >= 3) {
                return inventory.isItemValidForSlot(16 + i - 3, itemStack);
            } else {
                return inventory.isItemValidForSlot(i + 4, itemStack);
            }
        }
    }

    private static final int[] slotsTop = new int[]{0, 1, 2};
    private static final int[] slotsSide = new int[]{3};
    private static final int[] slotsBottom = new int[]{4, 5, 6};
    private static final int[] slotsCenter = new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final int SLOT_CENTER_OFFSET = 7;
    private static final int COOK_TIME = 200;

    private EntityItem[] renderItem = new EntityItem[4];
    private EntityItem interiorRenderItem;
    private ItemStack[] inventory = new ItemStack[20];
    private OvenInventory ovenInventory = new OvenInventory(this);
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int[] slotCookTime = new int[9];
    private float prevDoorAngle;
    private float doorAngle;
    private int numPlayersUsing;

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);

        interiorRenderItem = new EntityItem(world, 0, 0, 0);
        interiorRenderItem.hoverStart = 0f;
        for(int i = 0; i < renderItem.length; i++) {
            renderItem[i] = new EntityItem(world, 0, 0, 0);
            renderItem[i].hoverStart = 0f;
            renderItem[i].setEntityItemStack(inventory[16 + i]);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        switch (ForgeDirection.getOrientation(side)) {
            case UP:
                return slotsTop;
            case DOWN:
                return slotsBottom;
            default:
                return slotsSide;
        }
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int side) {
        return isItemValidForSlot(i, itemStack);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int side) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (inventory[i] != null) {
            ItemStack itemstack = inventory[i];
            inventory[i] = null;
            markDirty();
            return itemstack;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if (i >= SLOT_CENTER_OFFSET && i < SLOT_CENTER_OFFSET + 9) {
            slotCookTime[i - SLOT_CENTER_OFFSET] = 0;
        }
        if (inventory[i] != null) {
            ItemStack itemstack;
            if (inventory[i].stackSize <= count) {
                itemstack = inventory[i];
                inventory[i] = null;
                markDirty();
                return itemstack;
            } else {
                itemstack = inventory[i].splitStack(count);
                if (inventory[i].stackSize == 0) {
                    inventory[i] = null;
                }
                markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory[i] = itemStack;
        if(i >= 16 && i < 20) {
            renderItem[i - 16].setEntityItemStack(itemStack);
        }
        if(itemStack == null && i >= SLOT_CENTER_OFFSET && i < SLOT_CENTER_OFFSET + 9) {
            slotCookTime[i - SLOT_CENTER_OFFSET] = 0;
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "container.cookingbook:cookingoven";
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
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        switch (i) {
            case 0:
            case 1:
            case 2:
                return itemStack.getItem() instanceof ItemFood;
            case 3:
            case 4:
            case 5:
                return isItemFuel(itemStack);
        }
        return false;
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        if(id == 1) {
            numPlayersUsing = value;
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
    public void updateEntity() {
        super.updateEntity();

        boolean hasChanged = false;

        if (furnaceBurnTime > 0) {
            furnaceBurnTime--;
        }

        prevDoorAngle = doorAngle;
        if(numPlayersUsing > 0) {
            final float doorSpeed = 0.2f;
            doorAngle = Math.min(1f, doorAngle + doorSpeed);
        } else {
            final float doorSpeed = 0.1f;
            doorAngle = Math.max(0f, doorAngle - doorSpeed);
        }

        if (!worldObj.isRemote) {
            if (furnaceBurnTime == 0 && canCook()) {
                // Check for fuel items in side slots
                for (int j : slotsSide) {
                    if (getStackInSlot(j) != null) {
                        ItemStack fuelItem = getStackInSlot(j);
                        currentItemBurnTime = furnaceBurnTime = (int) Math.max(1, (float) getItemBurnTime(fuelItem) / 3f);
                        if (furnaceBurnTime != 0) {
                            fuelItem.stackSize--;
                            if (fuelItem.stackSize == 0) {
                                setInventorySlotContents(j, fuelItem.getItem().getContainerItem(fuelItem));
                            }
                            hasChanged = true;
                        }
                        break;
                    }
                }
            }

            if (furnaceBurnTime == 0 && !worldObj.isRemote) {
                for (int i = 0; i < slotCookTime.length; i++) {
                    if (slotCookTime[i] > 0) {
                        slotCookTime[i] = 0;
                    }
                }
            }

            int firstEmptySlot = -1;
            int firstTransferSlot = -1;
            // Cook items
            for (int i : slotsCenter) {
                if (getStackInSlot(i) != null) {
                    if (slotCookTime[i - SLOT_CENTER_OFFSET] != -1) {
                        slotCookTime[i - SLOT_CENTER_OFFSET]++;
                        if (slotCookTime[i - SLOT_CENTER_OFFSET] >= COOK_TIME) {
                            ItemStack resultStack = getSmeltingResult(getStackInSlot(i));
                            if (resultStack != null) {
                                setInventorySlotContents(i, resultStack.copy());
                                slotCookTime[i - SLOT_CENTER_OFFSET] = -1;
                                if (firstTransferSlot == -1) {
                                    firstTransferSlot = i;
                                }
                            }
                        }
                    } else if (firstTransferSlot == -1) {
                        firstTransferSlot = i;
                    }
                } else if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            }

            // Move cooked items from center to bottom slots
            if (firstTransferSlot != -1) {
                ItemStack transferStack = getStackInSlot(firstTransferSlot);
                while (transferStack.stackSize > 0) {
                    int firstResultSlot = -1;
                    int resultSpaceLeft = 0;
                    for (int j : slotsBottom) {
                        ItemStack slotStack = getStackInSlot(j);
                        if (slotStack != null && slotStack.getItem() == transferStack.getItem()) {
                            resultSpaceLeft = slotStack.getMaxStackSize() - slotStack.stackSize;

                            if (resultSpaceLeft > 0) {
                                firstResultSlot = j;
                                break;
                            }
                        }
                    }
                    if (firstResultSlot == -1) {
                        for (int j : slotsBottom) {
                            if (getStackInSlot(j) == null) {
                                firstResultSlot = j;
                                resultSpaceLeft = transferStack.getMaxStackSize();
                                break;
                            }
                        }
                    }
                    if (firstResultSlot != -1) {
                        ItemStack resultSlotStack = getStackInSlot(firstResultSlot);
                        if (resultSlotStack != null) {
                            resultSlotStack.stackSize += Math.min(transferStack.stackSize, resultSpaceLeft);
                            transferStack.stackSize -= Math.min(transferStack.stackSize, resultSpaceLeft);
                        } else {
                            setInventorySlotContents(firstResultSlot, transferStack.copy());
                            transferStack.stackSize = 0;
                        }
                        if (transferStack.stackSize <= 0) {
                            setInventorySlotContents(firstTransferSlot, null);
                        }
                    } else {
                        break;
                    }
                }
                if(getStackInSlot(firstTransferSlot) == null) {
                    slotCookTime[firstTransferSlot - SLOT_CENTER_OFFSET] = 0;
                }
                hasChanged = true;
            }

            // Move cookable items from top to center slots
            if (firstEmptySlot != -1) {
                for (int j : slotsTop) {
                    ItemStack itemStack = getStackInSlot(j);
                    if (itemStack != null) {
                        setInventorySlotContents(firstEmptySlot, itemStack.splitStack(1));
                        if (itemStack.stackSize <= 0) {
                            setInventorySlotContents(j, null);
                        }
                        break;
                    }
                }
            }
        }

        if (hasChanged) {
            markDirty();
        }
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        ItemStack result = CookingRegistry.getSmeltingResult(itemStack);
        if(result != null) {
            return result;
        }
        return FurnaceRecipes.smelting().getSmeltingResult(itemStack);
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return getItemBurnTime(itemStack) > 0;
    }

    public static int getItemBurnTime(ItemStack fuelItem) {
        int fuelTime = CookingRegistry.getOvenFuelTime(fuelItem);
        if(fuelTime != 0) {
            return fuelTime;
        }
        return TileEntityFurnace.getItemBurnTime(fuelItem);
    }

    private boolean canCook() {
        // Check for space or active smelting in center slots
        for (int k : slotsCenter) {
            ItemStack cookingStack = getStackInSlot(k);
            if (cookingStack != null && slotCookTime[k - SLOT_CENTER_OFFSET] != -1) {
                return true;
            } else if (cookingStack == null) {
                // Check for cookable items in top slots
                for (int i : slotsTop) {
                    if (getStackInSlot(i) != null) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("Items", 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
            byte slotId = itemCompound.getByte("Slot");
            if (slotId >= 0 && slotId < inventory.length) {
                inventory[slotId] = ItemStack.loadItemStackFromNBT(itemCompound);
                if(slotId >= 16 && slotId < 20 && renderItem[slotId - 16] != null) {
                    renderItem[slotId - 16].setEntityItemStack(inventory[slotId]);
                }
            }
        }
        furnaceBurnTime = compound.getShort("BurnTime");
        currentItemBurnTime = compound.getShort("CurrentItemBurnTime");
        slotCookTime = compound.getIntArray("CookTimes");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null) {
                NBTTagCompound itemCompound = new NBTTagCompound();
                itemCompound.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(itemCompound);
                tagList.appendTag(itemCompound);
            }
        }
        compound.setTag("Items", tagList);
        compound.setShort("BurnTime", (short) furnaceBurnTime);
        compound.setShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        compound.setIntArray("CookTimes", ArrayUtils.clone(slotCookTime));
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

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        return (float) furnaceBurnTime / (float) currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        return (float) slotCookTime[i] / (float) COOK_TIME;
    }

    public EntityItem getRenderItem(int i) {
        return renderItem[i];
    }

    @Override
    public IInventory getInventory() {
        return ovenInventory;
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        int[] inputSlots = getAccessibleSlotsFromSide(ForgeDirection.UP.ordinal());
        int firstEmptySlot = -1;
        for(int slot : inputSlots) {
            ItemStack slotStack = getStackInSlot(slot);
            if(slotStack != null) {
                if(slotStack.isItemEqual(slotStack)) {
                    int spaceLeft = Math.min(itemStack.stackSize, slotStack.getMaxStackSize() - slotStack.stackSize);
                    if(spaceLeft > 0) {
                        slotStack.stackSize += spaceLeft;
                        itemStack.stackSize -= spaceLeft;
                    }
                }
                if(itemStack.stackSize <= 0) {
                    return null;
                }
            } else if(firstEmptySlot == -1) {
                firstEmptySlot = slot;
            }
        }
        if(firstEmptySlot != -1) {
            setInventorySlotContents(firstEmptySlot, itemStack);
            return null;
        }
        return itemStack;
    }

    public float getPrevDoorAngle() {
        return prevDoorAngle;
    }

    public float getDoorAngle() {
        return doorAngle;
    }

    public EntityItem getInteriorRenderItem() {
        return interiorRenderItem;
    }
}
