package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableContainer;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.ChickenSinkMenu;
import net.blay09.mods.cookingforblockheads.rules.CookingForBlockheadsRules;
import net.minecraft.core.BlockPos;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class ChickenSinkBlockEntity extends BlockEntity implements BalmMenuProvider<Unit>, IMutableNameable, BalmContainerProvider, KitchenItemProviderHolder, TransferableBlockEntity<ChickenSinkBlockEntity.TransferData> {

    private static final int SYNC_INTERVAL = 10;

    private final DefaultContainer container = new DefaultContainer(5) {
        @Override
        public void setChanged() {
            ChickenSinkBlockEntity.this.setChanged();
        }
    };
    private final KitchenItemProvider itemProvider = new ContainerKitchenItemProvider(container);

    private boolean isDirty;
    private int ticksSinceSync;
    private int eggLayTime;
    private int eggLayTimeTarget = defaultEggLayTime();
    private @Nullable Component customName;

    public ChickenSinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.chickenSink.value(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChickenSinkBlockEntity blockEntity) {
        blockEntity.serverTick(level);
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
        customName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);
        eggLayTime = input.getIntOr("EggLayTime", 0);
        eggLayTimeTarget = input.getIntOr("EggLayTimeTarget", resolveEggLayTimeTarget());
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.storeNullable("CustomName", ComponentSerialization.CODEC, customName);
        output.putInt("EggLayTime", eggLayTime);
        output.putInt("EggLayTimeTarget", eggLayTimeTarget);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    public void serverTick(Level level) {
        if (eggLayTime < eggLayTimeTarget) {
            eggLayTime++;
            setChanged();
        } else if (CookingForBlockheadsRules.chickenSinkMayLayEgg.getOrDefault(this) && tryProduceEgg()) {
            eggLayTime = 0;
            eggLayTimeTarget = resolveEggLayTimeTarget();
            setChanged();
            level.playSound(null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.75f, Mth.nextFloat(level.getRandom(), 0.9f, 1.1f));
        }

        ticksSinceSync++;
        if (ticksSinceSync >= SYNC_INTERVAL) {
            ticksSinceSync = 0;
            if (isDirty) {
                BalmBlockEntityUtils.sync(this);
                isDirty = false;
            }
        }
    }

    private boolean tryProduceEgg() {
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            if (slotStack.isEmpty()) {
                container.setItem(i, new ItemStack(Items.EGG));
                return true;
            }
            if (slotStack.is(Items.EGG) && slotStack.getCount() < slotStack.getMaxStackSize()) {
                slotStack.grow(1);
                container.setChanged();
                return true;
            }
        }
        return false;
    }

    public int defaultEggLayTime() {
        final int minTime = 6000;
        final int maxTime = 12000;
        return minTime + levelRandom().nextInt(maxTime - minTime + 1);
    }

    private int resolveEggLayTimeTarget() {
        return Math.max(1, CookingForBlockheadsRules.chickenSinkEggLayTime.getOrDefault(this));
    }

    private RandomSource levelRandom() {
        return level != null ? level.getRandom() : RandomSource.create();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new ChickenSinkMenu(i, playerInventory, container, ContainerLevelAccess.create(level, worldPosition));
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
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
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.chicken_sink");
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Unit> getScreenStreamCodec() {
        return Unit.STREAM_CODEC.cast();
    }

    @Override
    public Unit getScreenOpeningData(ServerPlayer serverPlayer) {
        return Unit.INSTANCE;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        isDirty = true;
    }

    @Override
    public TransferData snapshotDataForTransfer() {
        return new TransferData(TransferableContainer.copyAndClear(container), eggLayTime, eggLayTimeTarget);
    }

    @Override
    public void restoreFromTransferSnapshot(TransferData data) {
        data.container().applyTo(container);
        eggLayTime = data.eggLayTime();
        eggLayTimeTarget = data.eggLayTimeTarget();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        dropItems(level, pos);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

    public record TransferData(TransferableContainer container, int eggLayTime, int eggLayTimeTarget) {
    }
}
