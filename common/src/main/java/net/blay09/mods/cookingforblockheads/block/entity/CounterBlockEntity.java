package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.blay09.mods.cookingforblockheads.block.CounterBlock;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.kitchen.ConditionalKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ConversingKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.CounterMenu;
import net.blay09.mods.cookingforblockheads.block.entity.util.DoorAnimator;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class CounterBlockEntity extends BlockEntity implements BalmMenuProvider<BlockPos>, IMutableNameable, BalmContainerProvider, UpgradeablePreservation, KitchenItemProviderHolder {

    private final int containerSize = CookingForBlockheadsConfig.getActive().largeCounters ? 54 : 27;

    protected final DefaultContainer container = new DefaultContainer(containerSize) {
        @Override
        public void setChanged() {
            isDirty = true;
            CounterBlockEntity.this.setChanged();
        }
    };

    protected final DoorAnimator doorAnimator = new DoorAnimator(this, 1, 2);

    protected boolean hasPreservationUpgrade;
    private @Nullable Component customName;

    private boolean isDirty;

    private final ContainerKitchenItemProvider conservingItemProvider = new ConversingKitchenItemProvider(container);
    private final ContainerKitchenItemProvider containerItemProvider = new ContainerKitchenItemProvider(container);
    private final KitchenItemProvider itemProvider = new ConditionalKitchenItemProvider<>(this::hasPreservationUpgrade,
            conservingItemProvider,
            containerItemProvider);

    public CounterBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.counter.value(), pos, state);
    }

    public CounterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        doorAnimator.setOpenRadius(2);
        doorAnimator.setSoundEventOpen(SoundEvents.CHEST_OPEN);
        doorAnimator.setSoundEventClose(SoundEvents.CHEST_CLOSE);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CounterBlockEntity blockEntity) {
        blockEntity.clientTick(level, pos, state);
    }

    public void clientTick(Level level, BlockPos pos, BlockState state) {
        doorAnimator.update();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CounterBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (isDirty) {
            BalmBlockEntityUtils.sync(this);
            isDirty = false;
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        return doorAnimator.receiveClientEvent(id, type) || super.triggerEvent(id, type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter input) {
        final var customNameComponent = input.get(DataComponents.CUSTOM_NAME);
        if (customNameComponent != null) {
            customName = customNameComponent;
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.CUSTOM_NAME, customName);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.child("ItemHandler").ifPresent(it -> ContainerHelper.loadAllItems(it, container.getItems()));
        hasPreservationUpgrade = input.getBooleanOr("HasPreservationUpgrade", false);
        customName = input.read("CustomNameV2", ComponentSerialization.CODEC).orElse(null);
        doorAnimator.setForcedOpen(input.getBooleanOr("IsForcedOpen", false));
        doorAnimator.setNumPlayersUsing(input.getByteOr("NumPlayersUsing", (byte) 0));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);
        output.storeNullable("CustomNameV2", ComponentSerialization.CODEC, customName);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, output -> {
            saveAdditional(output);
            output.putBoolean("IsForcedOpen", doorAnimator.isForcedOpen());
            output.putByte("NumPlayersUsing", (byte) doorAnimator.getNumPlayersUsing());
        });
    }

    public DoorAnimator getDoorAnimator() {
        return doorAnimator;
    }

    public Direction getFacing() {
        BlockState blockState = getBlockState();
        return blockState.hasProperty(BlockStateProperties.FACING) ? blockState.getValue(BlockStateProperties.FACING) : Direction.NORTH;
    }

    public boolean isFlipped() {
        BlockState blockState = getBlockState();
        return blockState.hasProperty(CounterBlock.FLIPPED) && blockState.getValue(CounterBlock.FLIPPED);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new CounterMenu(i, playerInventory, this);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-1, 0, -1).getCenter(), worldPosition.offset(2, 1, 2).getCenter());
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

    @Override
    public @Nullable Component getCustomName() {
        return customName;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.counter");
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

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
        return worldPosition;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public boolean hasPreservationUpgrade() {
        return hasPreservationUpgrade;
    }

    @Override
    public void setHasPreservationUpgrade(boolean hasPreservationUpgrade) {
        this.hasPreservationUpgrade = hasPreservationUpgrade;
        setChanged();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        dropItems(level, pos);
        if (hasPreservationUpgrade) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, ModItems.preservationChamber.createStack());
        }
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

}
