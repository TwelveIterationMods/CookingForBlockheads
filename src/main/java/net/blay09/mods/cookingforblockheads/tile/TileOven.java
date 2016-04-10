package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingConfig;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.apache.commons.lang3.ArrayUtils;

public class TileOven extends TileEntity implements ITickable, IKitchenSmeltingProvider {

    private static final int COOK_TIME = 200;

    private final ItemStackHandler itemHandler = new ItemStackHandler(20) {
        @Override
        protected void onContentsChanged(int slot) {
            if(slot >= 7 && slot < 16) {
                slotCookTime[slot - 7] = 0;
            }
            isDirty = true;
            markDirty();
        }
    };
    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 3);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 3, 4);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 4, 7);
    private final RangedWrapper itemHandlerProcessing = new RangedWrapper(itemHandler, 7, 16);
    private final RangedWrapper itemHandlerTools = new RangedWrapper(itemHandler, 16, 20);
    private final KitchenItemProvider itemProvider = new KitchenItemProvider(itemHandlerOutput);
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    public int[] slotCookTime = new int[9];
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Override
    public void update() {
        doorAnimator.update();

        if(isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            isDirty = false;
        }

        boolean hasChanged = false;

        if (furnaceBurnTime > 0) {
            furnaceBurnTime--;
        }

        if (!worldObj.isRemote) {
            if (furnaceBurnTime == 0 && shouldConsumeFuel()) {
                // Check for fuel items in side slots
                for (int i = 0; i < itemHandlerFuel.getSlots(); i++) {
                    ItemStack fuelItem = itemHandlerFuel.getStackInSlot(i);
                    if (fuelItem != null) {
                        currentItemBurnTime = furnaceBurnTime = (int) Math.max(1, (float) getItemBurnTime(fuelItem) * CookingConfig.ovenFuelTimeMultiplier);
                        if (furnaceBurnTime != 0) {
                            fuelItem.stackSize--;
                            if (fuelItem.stackSize == 0) {
                                itemHandlerFuel.setStackInSlot(i, fuelItem.getItem().getContainerItem(fuelItem));
                            }
                            hasChanged = true;
                        }
                        break;
                    }
                }
            }

            int firstEmptySlot = -1;
            int firstTransferSlot = -1;
            for (int i = 0; i < itemHandlerProcessing.getSlots(); i++) {
                ItemStack itemStack = itemHandlerProcessing.getStackInSlot(i);

                if (itemStack != null) {
                    if (slotCookTime[i] != -1) {
                        if(furnaceBurnTime > 0) {
                            slotCookTime[i]++;
                        }
                        if (slotCookTime[i] >= COOK_TIME * CookingConfig.ovenCookTimeMultiplier) {
                            ItemStack resultStack = getSmeltingResult(itemStack);
                            if (resultStack != null) {
                                itemHandlerProcessing.setStackInSlot(i, resultStack.copy());
                                slotCookTime[i] = -1;
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

            // Move cooked items from processing to output
            if (firstTransferSlot != -1) {
                ItemStack transferStack = itemHandlerProcessing.getStackInSlot(firstTransferSlot);
                transferStack = ItemHandlerHelper.insertItemStacked(itemHandlerOutput, transferStack, false);
                itemHandlerProcessing.setStackInSlot(firstTransferSlot, transferStack);
                if(transferStack == null) {
                    slotCookTime[firstTransferSlot] = 0;
                }
                hasChanged = true;
            }

            // Move cookable items from input to processing
            if (firstEmptySlot != -1) {
                for (int j = 0; j < itemHandlerInput.getSlots(); j++) {
                    ItemStack itemStack = itemHandlerInput.getStackInSlot(j);
                    if (itemStack != null) {
                        itemHandlerProcessing.setStackInSlot(firstEmptySlot, itemStack.splitStack(1));
                        if (itemStack.stackSize <= 0) {
                            itemHandlerInput.setStackInSlot(j, null);
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
        return FurnaceRecipes.instance().getSmeltingResult(itemStack);
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return getItemBurnTime(itemStack) > 0;
    }

    public static int getItemBurnTime(ItemStack fuelItem) {
        int fuelTime = CookingRegistry.getOvenFuelTime(fuelItem);
        if(fuelTime != 0 || CookingConfig.ovenRequiresCookingOil) {
            return fuelTime;
        }
        return TileEntityFurnace.getItemBurnTime(fuelItem);
    }

    private boolean shouldConsumeFuel() {
        for (int i = 0; i < itemHandlerProcessing.getSlots(); i++) {
            ItemStack cookingStack = itemHandlerProcessing.getStackInSlot(i);
            if (cookingStack != null && slotCookTime[i] != -1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
        furnaceBurnTime = tagCompound.getShort("BurnTime");
        currentItemBurnTime = tagCompound.getShort("CurrentItemBurnTime");
        slotCookTime = tagCompound.getIntArray("CookTimes");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        tagCompound.setShort("BurnTime", (short) furnaceBurnTime);
        tagCompound.setShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        tagCompound.setIntArray("CookTimes", ArrayUtils.clone(slotCookTime));
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        tagCompound.setBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
        tagCompound.setByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        return new SPacketUpdateTileEntity(pos, 0, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
        doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        return (float) furnaceBurnTime / (float) currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        return (float) slotCookTime[i] / ((float) COOK_TIME * CookingConfig.ovenCookTimeMultiplier);
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        return ItemHandlerHelper.insertItemStacked(itemHandlerInput, itemStack, false);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public ItemStack getToolItem(int i) {
        return itemHandlerTools.getStackInSlot(i);
    }

    public void setToolItem(int i, ItemStack itemStack) {
        itemHandlerTools.setStackInSlot(i, itemStack);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityKitchenSmeltingProvider.KITCHEN_SMELTING_PROVIDER_CAPABILITY
                || capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(facing == null) {
                return (T) itemHandler;
            }
            switch(facing) {
                case UP:
                    return (T) itemHandlerInput;
                case DOWN:
                    return (T) itemHandlerOutput;
                default:
                    return (T) itemHandlerFuel;
            }
        }
        if(capability == CapabilityKitchenItemProvider.KITCHEN_ITEM_PROVIDER_CAPABILITY) {
            return (T) itemProvider;
        }
        if(capability == CapabilityKitchenSmeltingProvider.KITCHEN_SMELTING_PROVIDER_CAPABILITY) {
            return (T) this;
        }
        return super.getCapability(capability, facing);
    }

    public IItemHandler getInputHandler() {
        return itemHandlerInput;
    }
}
