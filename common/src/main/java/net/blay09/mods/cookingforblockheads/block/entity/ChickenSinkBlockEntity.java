package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.*;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableContainer;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.ChickenSinkMenu;
import net.blay09.mods.cookingforblockheads.rules.CookingForBlockheadsRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jspecify.annotations.Nullable;

public class ChickenSinkBlockEntity extends BlockEntity implements BalmMenuProvider<Unit>, IMutableNameable, BalmContainerProvider, KitchenItemProviderHolder, TransferableBlockEntity<ChickenSinkBlockEntity.TransferData> {

    private static final int SYNC_INTERVAL = 10;

    private final DefaultContainer container = new DefaultContainer(6) {
        @Override
        public void setChanged() {
            ChickenSinkBlockEntity.this.setChanged();
        }
    };
    private final SubContainer feedContainer = new SubContainer(container, 0, 1);
    private final SubContainer eggContainer = new SubContainer(container, 1, 5);
    private final KitchenItemProvider itemProvider = new ContainerKitchenItemProvider(eggContainer);

    private boolean isDirty;
    private int ticksSinceSync;
    private int eggLayTime;
    private int eggLayTimeTarget = defaultEggLayTime();
    private @Nullable Component customName;
    private @Nullable Holder<ChickenVariant> chickenType;
    private @Nullable BlockPos jukebox;
    private int partyBpm;

    public ChickenSinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.chickenSink.value(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChickenSinkBlockEntity blockEntity) {
        blockEntity.serverTick(level);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, ChickenSinkBlockEntity blockEntity) {
        final var jukebox = blockEntity.jukebox;
        if (jukebox == null || !jukebox.closerToCenterThan(pos.getCenter(), 3.46f) || !level.getBlockState(jukebox).is(Blocks.JUKEBOX)) {
            blockEntity.partyBpm = 0;
            blockEntity.jukebox = null;
        }
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
        chickenType = input.read("ChickenType", ChickenVariant.CODEC).orElse(null);
        eggLayTime = input.getIntOr("EggLayTime", 0);
        eggLayTimeTarget = input.getIntOr("EggLayTimeTarget", resolveEggLayTimeTarget(false));
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.storeNullable("CustomName", ComponentSerialization.CODEC, customName);
        output.storeNullable("ChickenType", ChickenVariant.CODEC, chickenType);
        output.putInt("EggLayTime", eggLayTime);
        output.putInt("EggLayTimeTarget", eggLayTimeTarget);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    public void serverTick(Level level) {
        if (chickenType != null) {
            if (eggLayTime < eggLayTimeTarget) {
                eggLayTime++;
                setChanged();
            } else if (CookingForBlockheadsRules.chickenSinkMayLayEgg.getOrDefault(this) && tryProduceEgg(level)) {
                eggLayTime = 0;
                eggLayTimeTarget = resolveEggLayTimeTarget(true);
                setChanged();
                level.playSound(null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.75f, Mth.nextFloat(level.getRandom(), 0.9f, 1.1f));
            }
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

    private boolean tryProduceEgg(Level level) {
        if (!(level instanceof ServerLevel serverLevel) || chickenType == null) {
            return false;
        }

        final var chicken = new Chicken(EntityType.CHICKEN, serverLevel);
        chicken.setVariant(chickenType);
        chicken.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
        return chicken.dropFromGiftLootTable(serverLevel, BuiltInLootTables.CHICKEN_LAY,
                (_, itemStack) -> ContainerUtils.insertItemStacked(eggContainer, itemStack, false));
    }

    public int defaultEggLayTime() {
        final int minTime = 12000;
        final int maxTime = 24000;
        return minTime + levelRandom().nextInt(maxTime - minTime + 1);
    }

    private int resolveEggLayTimeTarget(boolean consumeFeed) {
        final var baseEggLayTime = Math.max(1, CookingForBlockheadsRules.chickenSinkEggLayTime.getOrDefault(this));
        if (consumeFeed && tryConsumeFeed()) {
            return Mth.ceil(baseEggLayTime / 2f);
        }

        return baseEggLayTime;
    }

    private boolean tryConsumeFeed() {
        final var seedStack = feedContainer.getItem(0);
        if (seedStack.is(ItemTags.CHICKEN_FOOD)) {
            seedStack.shrink(1);
            feedContainer.setChanged();
            return true;
        }

        return false;
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

    public @Nullable Holder<ChickenVariant> getChickenType() {
        return chickenType;
    }

    public void setChickenType(@Nullable Holder<ChickenVariant> chickenType) {
        this.chickenType = chickenType;
        setChanged();
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
        return new TransferData(TransferableContainer.copyAndClear(container), eggLayTime, eggLayTimeTarget, chickenType);
    }

    @Override
    public void restoreFromTransferSnapshot(TransferData data) {
        data.container().applyTo(container);
        eggLayTime = data.eggLayTime();
        eggLayTimeTarget = data.eggLayTimeTarget();
        chickenType = data.chickenType();
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

    public boolean isPartying() {
        return partyBpm > 0;
    }

    public void setRecordPlayingNearby(BlockPos pos, boolean playing) {
        this.jukebox = pos;
        this.partyBpm = playing ? 85 : 0;
    }

    public record TransferData(TransferableContainer container, int eggLayTime, int eggLayTimeTarget,
                               @Nullable Holder<ChickenVariant> chickenType) {
    }
}
