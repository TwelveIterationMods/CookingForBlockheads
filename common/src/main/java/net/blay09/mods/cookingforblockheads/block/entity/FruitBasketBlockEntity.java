package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.DefaultContainer;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.kitchen.ConditionalKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ConversingKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.FruitBasketMenu;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
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
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class FruitBasketBlockEntity extends BlockEntity implements BalmMenuProvider<BlockPos>, IMutableNameable, BalmContainerProvider, UpgradeablePreservation, KitchenItemProviderHolder {

    private boolean hasPreservationUpgrade;
    private Component customName;
    private boolean isDirty;
    private final DefaultContainer container = new DefaultContainer(27) {
        @Override
        public void setChanged() {
            FruitBasketBlockEntity.this.setChanged();
            BalmBlockEntityUtils.sync(FruitBasketBlockEntity.this);
        }
    };
    private final ContainerKitchenItemProvider conservingItemProvider = new ConversingKitchenItemProvider(container);
    private final ContainerKitchenItemProvider containerItemProvider = new ContainerKitchenItemProvider(container);
    private final KitchenItemProvider itemProvider = new ConditionalKitchenItemProvider<>(this::hasPreservationUpgrade,
            conservingItemProvider,
            containerItemProvider);

    public FruitBasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.fruitBasket.value(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FruitBasketBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
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
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output.child("ItemHandler"), container.getItems());
        output.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, customName);
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    @Override
    public Component getName() {
        return customName != null ? customName : getDefaultName();
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
    public void setCustomName(Component customName) {
        this.customName = customName;
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new FruitBasketMenu(i, playerInventory, this);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.fruit_basket");
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (isDirty) {
            BalmBlockEntityUtils.sync(this);
            isDirty = false;
        }
    }

    @Override
    public void setChanged() {
        isDirty = true;
        super.setChanged();
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
        return worldPosition;
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
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        dropItems(level, pos);
        if (hasPreservationUpgrade()) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, ModItems.preservationChamber.createStack());
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }
}
