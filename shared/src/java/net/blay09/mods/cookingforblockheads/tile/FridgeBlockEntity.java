package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.CombinedContainer;
import net.blay09.mods.balm.api.container.ContainerUtils;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.cookingforblockheads.ModSounds;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.FridgeBlock;
import net.blay09.mods.cookingforblockheads.menu.FridgeMenu;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.util.DoorAnimator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FridgeBlockEntity extends BalmBlockEntity implements BalmMenuProvider, IMutableNameable, BalmContainerProvider {

    private final DefaultContainer container = new DefaultContainer(27) {
        @Override
        public void setChanged() {
            isDirty = true;
            FridgeBlockEntity.this.setChanged();
        }
    };

    private final DefaultKitchenItemProvider itemProvider = new DefaultKitchenItemProvider(container) {

        private final ItemStack snowStack = new ItemStack(Items.SNOWBALL);
        private final ItemStack iceStack = new ItemStack(Blocks.ICE);

        @Nullable
        private SourceItem applyIceUnit(IngredientPredicate predicate, int maxAmount) {
            if (getBaseFridge().hasIceUpgrade && predicate.test(snowStack, 64)) {
                return new SourceItem(this, -1, ContainerUtils.copyStackWithSize(snowStack, maxAmount));
            }

            if (getBaseFridge().hasIceUpgrade && predicate.test(iceStack, 64)) {
                return new SourceItem(this, -1, ContainerUtils.copyStackWithSize(iceStack, maxAmount));
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

            IngredientPredicate modifiedPredicate = predicate;
            if (getBaseFridge().hasPreservationUpgrade) {
                modifiedPredicate = (it, count) -> (count > 1 || !Balm.getHooks().getCraftingRemainingItem(it).isEmpty() || CookingRegistry.isToolItem(it)) && predicate.test(it, count);
            }

            return super.findSource(modifiedPredicate, maxAmount, inventories, requireBucket, simulate);
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
                modifiedPredicate = (it, count) -> (count > 1 || !Balm.getHooks().getCraftingRemainingItem(it).isEmpty() || CookingRegistry.isToolItem(it)) && predicate.test(it, count);
            }

            return super.findSourceAndMarkAsUsed(modifiedPredicate, maxAmount, inventories, requireBucket, simulate);
        }
    };

    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private Component customName;

    private boolean isDirty;
    public boolean hasIceUpgrade;
    public boolean hasPreservationUpgrade;

    public FridgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fridge.get(), pos, state);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(ModSounds.fridgeOpen.get());
        doorAnimator.setSoundEventClose(ModSounds.fridgeClose.get());
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

    public void tick() { // TODO
        doorAnimator.update();

        if (isDirty) {
            balmSync();
            isDirty = false;
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.triggerEvent(id, type);
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        container.deserialize(tagCompound.getCompound("ItemHandler"));
        hasIceUpgrade = tagCompound.getBoolean("HasIceUpgrade");
        hasPreservationUpgrade = tagCompound.getBoolean("HasPreservationUpgrade");

        if (tagCompound.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());
        tagCompound.putBoolean("HasIceUpgrade", hasIceUpgrade);
        tagCompound.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);

        if (customName != null) {
            tagCompound.putString("CustomName", Component.Serializer.toJson(customName));
        }

        return tagCompound;
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
        super.balmFromClientTag(tag);
        doorAnimator.setForcedOpen(tag.getBoolean("IsForcedOpen"));
        doorAnimator.setNumPlayersUsing(tag.getByte("NumPlayersUsing"));
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        tag.putBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
        tag.putByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        return tag;
    }

    @Nullable
    public FridgeBlockEntity findNeighbourFridge() {
        if (level.getBlockState(worldPosition.above()).getBlock() instanceof FridgeBlock) {
            return (FridgeBlockEntity) level.getBlockEntity(worldPosition.above());
        } else if (level.getBlockState(worldPosition.below()).getBlock() instanceof FridgeBlock) {
            return (FridgeBlockEntity) level.getBlockEntity(worldPosition.below());
        }

        return null;
    }

    public FridgeBlockEntity getBaseFridge() {
        if (!hasLevel()) {
            return this;
        }

        if (level.getBlockState(worldPosition.below()).getBlock() instanceof FridgeBlock) {
            FridgeBlockEntity baseFridge = (FridgeBlockEntity) level.getBlockEntity(worldPosition.below());
            if (baseFridge != null) {
                return baseFridge;
            }
        }

        return this;
    }

    /*@Override TODO
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, getCombinedItemHandlerCapability());
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        }

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }*/

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public Container getCombinedContainer() {
        FridgeBlockEntity baseFridge = getBaseFridge();
        FridgeBlockEntity neighbourFridge;
        if (baseFridge == this) {
            neighbourFridge = findNeighbourFridge();
        } else {
            neighbourFridge = this;
        }

        if (neighbourFridge != null) {
            return new CombinedContainer(neighbourFridge.container, baseFridge.container);
        }

        return container;
    }

    @Override
    public AABB balmGetRenderBoundingBox() {
        return new AABB(worldPosition.offset(-1, 0, -1), worldPosition.offset(2, 2, 2));
    }

    public void markDirtyAndUpdate() {
        BlockState state = level.getBlockState(worldPosition);
        level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), state, state, 3, 512);
        setChanged();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new FridgeMenu(i, playerInventory, this);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
    }

    @Override
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public boolean hasCustomName() {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getDefaultName() {
        return new TranslatableComponent("container.cookingforblockheads.fridge");
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
