package net.blay09.mods.cookingbook.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCookingOven extends TileEntity implements ISidedInventory {

    private static final int[] slotsTop = new int[]{0, 1, 2};
    private static final int[] slotsSide = new int[]{3};
    private static final int[] slotsBottom = new int[]{4, 5, 6};
    private static final int[] slotsCenter = new int[]{7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final int SLOT_CENTER_OFFSET = 7;
    private static final int COOK_TIME = 200;

    private ItemStack[] inventory = new ItemStack[16];
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int[] slotCookTime = new int[9];

    @Override
    public int[] getSlotsForFace(int side) {
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
            return itemstack;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if (i >= SLOT_CENTER_OFFSET) {
            slotCookTime[i - SLOT_CENTER_OFFSET] = 0;
        }
        if (inventory[i] != null) {
            ItemStack itemstack;
            if (inventory[i].stackSize <= count) {
                itemstack = inventory[i];
                inventory[i] = null;
                return itemstack;
            } else {
                itemstack = inventory[i].splitStack(count);
                if (inventory[i].stackSize == 0) {
                    inventory[i] = null;
                }
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory[i] = itemStack;
    }

    @Override
    public String getInventoryName() {
        return "container.cookingbook:cookingoven";
    }

    @Override
    public boolean isCustomInventoryName() {
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
    public void openChest() {
    }

    @Override
    public void closeChest() {
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
                return TileEntityFurnace.isItemFuel(itemStack);
        }
        return false;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        boolean hasChanged = false;

        if (furnaceBurnTime > 0) {
            furnaceBurnTime--;
        }

        if (!worldObj.isRemote) {
            if (furnaceBurnTime == 0 && canCook()) {
                // Check for fuel items in side slots
                for (int j : slotsSide) {
                    if (getStackInSlot(j) != null) {
                        ItemStack fuelItem = getStackInSlot(j);
                        currentItemBurnTime = furnaceBurnTime = TileEntityFurnace.getItemBurnTime(fuelItem);
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
                            ItemStack resultStack = FurnaceRecipes.instance().getSmeltingResult(getStackInSlot(i));
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
                            firstResultSlot = j;
                            resultSpaceLeft = slotStack.getMaxStackSize() - slotStack.stackSize;
                            break;
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
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList tagList = compound.getTagList("Items", 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound itemCompound = tagList.getCompoundTagAt(i);
            byte slotId = itemCompound.getByte("Slot");
            if (slotId >= 0 && slotId < inventory.length) {
                inventory[slotId] = ItemStack.loadItemStackFromNBT(itemCompound);
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
        compound.setIntArray("CookTimes", slotCookTime);
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
}
