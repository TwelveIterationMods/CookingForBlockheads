package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.KitchenCounterBlock;
import net.blay09.mods.cookingforblockheads.menu.CounterMenu;
import net.blay09.mods.cookingforblockheads.tile.util.DoorAnimator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class CounterBlockEntity extends BalmBlockEntity implements BalmMenuProvider, IMutableNameable, BalmContainerProvider {

    private final int containerSize = CookingForBlockheadsConfig.getActive().largeCounters ? 54 : 27;

    private final DefaultContainer container = new DefaultContainer(containerSize) {
        @Override
        public void setChanged() {
            CounterBlockEntity.this.setChanged();
        }
    };

    private final DefaultKitchenItemProvider itemProvider = new DefaultKitchenItemProvider(container);
    private final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    private Component customName;

    private boolean isFirstTick = true;

    private boolean isDirty;

    private DyeColor color = DyeColor.WHITE;

    private Direction cachedFacing;
    private boolean cachedFlipped;

    public CounterBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.counter.get(), pos, state);
    }

    public CounterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(SoundEvents.CHEST_OPEN);
        doorAnimator.setSoundEventClose(SoundEvents.CHEST_CLOSE);
    }

    public void serverTick() { // TODO
        if (isFirstTick) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof KitchenCounterBlock) { // looks like there's an issue here similar to TESRs where the state doesn't match the tile
                cachedFacing = state.getValue(KitchenCounterBlock.FACING);
                cachedFlipped = state.getValue(KitchenCounterBlock.FLIPPED);
                isFirstTick = false;
            }
        }

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
        CompoundTag itemHandlerCompound = tagCompound.getCompound("ItemHandler");
        if (CookingForBlockheadsConfig.getActive().largeCounters && itemHandlerCompound.getInt("Size") < 54) {
            itemHandlerCompound.putInt("Size", 54);
        }

        container.deserialize(itemHandlerCompound);

        color = DyeColor.byId(tagCompound.getByte("Color"));

        if (tagCompound.contains("CustomName", Tag.TAG_STRING)) {
            customName = Component.Serializer.fromJson(tagCompound.getString("CustomName"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        tagCompound.put("ItemHandler", container.serialize());
        tagCompound.putByte("Color", (byte) color.getId());

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

    /* TODO @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, itemHandlerCap);
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

    public Direction getFacing() {
        return cachedFacing == null ? Direction.NORTH : cachedFacing;
    }

    public boolean isFlipped() {
        return cachedFlipped;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new CounterMenu(i, playerInventory, this);
    }

    @Override
    public AABB balmGetRenderBoundingBox() {
        return new AABB(worldPosition.offset(-1, 0, -1), worldPosition.offset(2, 1, 2));
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
        return new TranslatableComponent("container.cookingforblockheads.counter");
    }

    @Override
    public void setChanged() {
        isDirty = true;
        super.setChanged();
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
