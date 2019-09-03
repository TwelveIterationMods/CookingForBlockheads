package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.network.VanillaPacketHandler;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.DoorAnimator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileFridge extends TileEntity implements ITickableTileEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(27) {
        @Override
        protected void onContentsChanged(int slot) {
            isDirty = true;
            markDirty();
        }
    };

    private final KitchenItemProvider itemProvider = new KitchenItemProvider(itemHandler) {

        private final ItemStack snowStack = new ItemStack(Items.SNOWBALL);
        private final ItemStack iceStack = new ItemStack(Blocks.ICE);

        @Nullable
        private SourceItem applyIceUnit(IngredientPredicate predicate, int maxAmount) {
            if (getBaseFridge().hasIceUpgrade && predicate.test(snowStack, 64)) {
                return new SourceItem(this, -1, ItemHandlerHelper.copyStackWithSize(snowStack, maxAmount));
            }

            if (getBaseFridge().hasIceUpgrade && predicate.test(iceStack, 64)) {
                return new SourceItem(this, -1, ItemHandlerHelper.copyStackWithSize(iceStack, maxAmount));
            }

            return null;
        }

        @Nullable
        @Override
        public SourceItem findSource(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
            SourceItem iceUnitResult = applyIceUnit(predicate, maxAmount);
            if (iceUnitResult != null) {
                return iceUnitResult;
            }

            return super.findSource(predicate, maxAmount, inventories, requireBucket, simulate);
        }

        @Nullable
        @Override
        public SourceItem findSourceAndMarkAsUsed(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
            SourceItem iceUnitResult = applyIceUnit(predicate, maxAmount);
            if (iceUnitResult != null) {
                return iceUnitResult;
            }

            IngredientPredicate modifiedPredicate = predicate;
            if (getBaseFridge().hasPreservationUpgrade) {
                modifiedPredicate = (it, count) -> (count > 1 || !it.getItem().getContainerItem(it).isEmpty() || CookingRegistry.isToolItem(it)) && predicate.test(it, count);
            }

            return super.findSourceAndMarkAsUsed(modifiedPredicate, maxAmount, inventories, requireBucket, simulate);
        }
    };

    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private final LazyOptional<IKitchenItemProvider> itemProviderCap = LazyOptional.of(() -> itemProvider);

    private DyeColor fridgeColor = DyeColor.WHITE;
    private boolean isDirty;
    public boolean hasIceUpgrade;
    public boolean hasPreservationUpgrade;

    public TileFridge() {
        super(ModTileEntities.fridge);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(ModSounds.fridgeOpen);
        doorAnimator.setSoundEventClose(ModSounds.fridgeClose);
    }

    public boolean hasIceUpgrade() {
        return hasIceUpgrade;
    }

    public void setHasIceUpgrade(boolean hasIceUpgrade) {
        this.hasIceUpgrade = hasIceUpgrade;
        markDirtyAndUpdate();
    }

    public boolean hasPreservationUpgrade() {
        return hasPreservationUpgrade;
    }

    public void setHasPreservationUpgrade(boolean hasPreservationUpgrade) {
        this.hasPreservationUpgrade = hasPreservationUpgrade;
        markDirtyAndUpdate();
    }

    public void setFridgeColor(DyeColor fridgeColor) {
        this.fridgeColor = fridgeColor;
        markDirtyAndUpdate();
    }

    @Override
    public void tick() {
        doorAnimator.update();

        if (isDirty) {
            VanillaPacketHandler.sendTileEntityUpdate(this);
            isDirty = false;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.receiveClientEvent(id, type);
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        fridgeColor = DyeColor.byId(tagCompound.getByte("FridgeColor"));
        hasIceUpgrade = tagCompound.getBoolean("HasIceUpgrade");
        hasPreservationUpgrade = tagCompound.getBoolean("HasPreservationUpgrade");
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        tagCompound.putByte("FridgeColor", (byte) fridgeColor.getId());
        tagCompound.putBoolean("HasIceUpgrade", hasIceUpgrade);
        tagCompound.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);
        return tagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
        doorAnimator.setForcedOpen(pkt.getNbtCompound().getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(pkt.getNbtCompound().getByte("NumPlayersUsing"));
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

    @Nullable
    public TileFridge findNeighbourFridge() {
        if (world.getBlockState(pos.up()).getBlock() == ModBlocks.fridge) {
            return (TileFridge) world.getTileEntity(pos.up());
        } else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.fridge) {
            return (TileFridge) world.getTileEntity(pos.down());
        }

        return null;
    }

    public TileFridge getBaseFridge() {
        if (!hasWorld()) {
            return this;
        }

        if (world.getBlockState(pos.down()).getBlock() == ModBlocks.fridge) {
            TileFridge baseFridge = (TileFridge) world.getTileEntity(pos.down());
            if (baseFridge != null) {
                return baseFridge;
            }
        }

        return this;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, getCombinedItemHandlerCapability());
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public DyeColor getFridgeColor() {
        return fridgeColor;
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public LazyOptional<IItemHandler> getCombinedItemHandlerCapability() {
        return LazyOptional.of(this::getCombinedItemHandler);
    }

    public IItemHandler getCombinedItemHandler() {
        TileFridge baseFridge = getBaseFridge();
        TileFridge neighbourFridge;
        if (baseFridge == this) {
            neighbourFridge = findNeighbourFridge();
        } else {
            neighbourFridge = this;
        }

        if (neighbourFridge != null) {
            return new CombinedInvWrapper(neighbourFridge.itemHandler, baseFridge.itemHandler);
        }

        return itemHandler;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
    }

    public void markDirtyAndUpdate() {
        BlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, world.getChunkAt(pos), state, state, 3);
        markDirty();
    }

}
