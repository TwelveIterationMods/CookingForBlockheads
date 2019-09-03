package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.BlockOven;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.DoorAnimator;
import net.blay09.mods.cookingforblockheads.tile.util.EnergyStorageModifiable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public class TileOven extends TileEntity implements ITickableTileEntity, IKitchenSmeltingProvider {

    private static final int COOK_TIME = 200;

    private final ItemStackHandler itemHandler = new ItemStackHandler(20) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot < 3) { // Input Slots
                if (getSmeltingResult(stack).isEmpty()) {
                    return stack;
                }
            } else if (slot == 3) {
                if (!isItemFuel(stack)) {
                    return stack;
                }
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (slot >= 7 && slot < 16) {
                slotCookTime[slot - 7] = 0;
            }
            isDirty = true;
            markDirty();
        }
    };

    private EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(10000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                markDirty();
            }

            return super.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!simulate) {
                markDirty();
            }

            return super.extractEnergy(maxExtract, simulate);
        }
    };

    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 3);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 3, 4);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 4, 7);
    private final RangedWrapper itemHandlerProcessing = new RangedWrapper(itemHandler, 7, 16);
    private final RangedWrapper itemHandlerTools = new RangedWrapper(itemHandler, 16, 20);
    private final KitchenItemProvider itemProvider = new KitchenItemProvider(new CombinedInvWrapper(itemHandlerTools, itemHandlerOutput));
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private boolean isFirstTick = true;

    public int[] slotCookTime = new int[9];
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    private boolean isDirty;

    private boolean hasPowerUpgrade;
    private Direction facing;

    public TileOven() {
        super(ModTileEntities.oven);
        doorAnimator.setSoundEventOpen(ModSounds.ovenOpen);
        doorAnimator.setSoundEventClose(ModSounds.ovenClose);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Override
    public void tick() {
        if (isFirstTick) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == ModBlocks.oven) {
                facing = state.get(BlockOven.FACING);
                isFirstTick = false;
            }
        }

        doorAnimator.update();

        if (isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            isDirty = false;
        }

        boolean hasChanged = false;

        int burnPotential = 200 - furnaceBurnTime;
        if (hasPowerUpgrade && burnPotential > 0 && shouldConsumeFuel()) {
            furnaceBurnTime += energyStorage.extractEnergy(burnPotential, false);
        }

        if (furnaceBurnTime > 0) {
            furnaceBurnTime--;
        }

        if (!world.isRemote) {
            if (furnaceBurnTime == 0 && shouldConsumeFuel()) {
                // Check for fuel items in side slots
                for (int i = 0; i < itemHandlerFuel.getSlots(); i++) {
                    ItemStack fuelItem = itemHandlerFuel.getStackInSlot(i);
                    if (!fuelItem.isEmpty()) {
                        currentItemBurnTime = furnaceBurnTime = (int) Math.max(1, (float) getItemBurnTime(fuelItem) * CookingForBlockheadsConfig.COMMON.ovenFuelTimeMultiplier.get());
                        if (furnaceBurnTime != 0) {
                            fuelItem.shrink(1);
                            if (fuelItem.getCount() == 0) {
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

                if (!itemStack.isEmpty()) {
                    if (slotCookTime[i] != -1) {
                        if (furnaceBurnTime > 0) {
                            slotCookTime[i]++;
                        }
                        if (slotCookTime[i] >= COOK_TIME * CookingForBlockheadsConfig.COMMON.ovenCookTimeMultiplier.get()) {
                            ItemStack resultStack = getSmeltingResult(itemStack);
                            if (!resultStack.isEmpty()) {
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
                if (transferStack.isEmpty()) {
                    slotCookTime[firstTransferSlot] = 0;
                }
                hasChanged = true;
            }

            // Move cookable items from input to processing
            if (firstEmptySlot != -1) {
                for (int j = 0; j < itemHandlerInput.getSlots(); j++) {
                    ItemStack itemStack = itemHandlerInput.getStackInSlot(j);
                    if (!itemStack.isEmpty()) {
                        itemHandlerProcessing.setStackInSlot(firstEmptySlot, itemStack.split(1));
                        if (itemStack.getCount() <= 0) {
                            itemHandlerInput.setStackInSlot(j, ItemStack.EMPTY);
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

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getEnergyCapacity() {
        return energyStorage.getMaxEnergyStored();
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        ItemStack result = CookingRegistry.getSmeltingResult(itemStack);
        if (!result.isEmpty()) {
            return result;
        }

        result = FurnaceRecipes.instance().getSmeltingResult(itemStack);
        if (!result.isEmpty() && result.getItem() instanceof ItemFood) {
            return result;
        }

        if (!result.isEmpty() && CookingRegistry.isNonFoodRecipe(result)) {
            return result;
        }

        return ItemStack.EMPTY;
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return getItemBurnTime(itemStack) > 0;
    }

    public static int getItemBurnTime(ItemStack fuelItem) {
        int fuelTime = CookingRegistry.getOvenFuelTime(fuelItem);
        if (fuelTime != 0 || CookingForBlockheadsConfig.COMMON.ovenRequiresCookingOil.get()) {
            return fuelTime;
        }
        return TileEntityFurnace.getItemBurnTime(fuelItem);
    }

    private boolean shouldConsumeFuel() {
        for (int i = 0; i < itemHandlerProcessing.getSlots(); i++) {
            ItemStack cookingStack = itemHandlerProcessing.getStackInSlot(i);
            if (!cookingStack.isEmpty() && slotCookTime[i] != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        furnaceBurnTime = tagCompound.getShort("BurnTime");
        currentItemBurnTime = tagCompound.getShort("CurrentItemBurnTime");
        slotCookTime = tagCompound.getIntArray("CookTimes");

        hasPowerUpgrade = tagCompound.getBoolean("HasPowerUpgrade");
        energyStorage.setEnergyStored(tagCompound.getInt("EnergyStored"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        tagCompound.putShort("BurnTime", (short) furnaceBurnTime);
        tagCompound.putShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        tagCompound.putIntArray("CookTimes", ArrayUtils.clone(slotCookTime));

        tagCompound.putBoolean("HasPowerUpgrade", hasPowerUpgrade);
        tagCompound.putInt("EnergyStored", energyStorage.getEnergyStored());
        return tagCompound;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tagCompound = new CompoundNBT();
        write(tagCompound);
        tagCompound.putBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
        tagCompound.putByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        return tagCompound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
        doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
    }

    public boolean hasPowerUpgrade() {
        return hasPowerUpgrade;
    }

    public void setHasPowerUpgrade(boolean hasPowerUpgrade) {
        this.hasPowerUpgrade = hasPowerUpgrade;
        markDirty();
        BlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, world.getChunkAt(pos), state, state, 3);
    }

    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }

    public float getBurnTimeProgress() {
        if (currentItemBurnTime == 0 && furnaceBurnTime > 0) {
            return 1f;
        }

        return (float) furnaceBurnTime / (float) currentItemBurnTime;
    }

    public float getCookProgress(int i) {
        return (float) slotCookTime[i] / ((float) (COOK_TIME * CookingForBlockheadsConfig.COMMON.ovenCookTimeMultiplier.get()));
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
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return (T) itemHandler;
            }

            if (!CookingForBlockheadsConfig.COMMON.disallowOvenAutomation.get()) {
                switch (facing) {
                    case UP:
                        return (T) itemHandlerInput;
                    case DOWN:
                        return (T) itemHandlerOutput;
                    default:
                        return (T) itemHandlerFuel;
                }
            }
        }

        if (hasPowerUpgrade && capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }

        if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
            return (T) itemProvider;
        }

        if (capability == CapabilityKitchenSmeltingProvider.CAPABILITY) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    public IItemHandler getInputHandler() {
        return itemHandlerInput;
    }

    public RangedWrapper getItemHandlerFuel() {
        return itemHandlerFuel;
    }

    public Direction getFacing() {
        return facing == null ? Direction.NORTH : facing;
    }
}
