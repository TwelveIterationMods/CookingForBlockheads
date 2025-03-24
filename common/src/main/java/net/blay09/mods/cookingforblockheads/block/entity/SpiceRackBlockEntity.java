package net.blay09.mods.cookingforblockheads.block.entity;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.kitchen.ConditionalKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ConversingKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.menu.SpiceRackMenu;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiceRackBlockEntity extends BalmBlockEntity implements BalmMenuProvider<BlockPos>, IMutableNameable, BalmContainerProvider, UpgradeablePreservation {

    private final DefaultContainer container = new DefaultContainer(9) {
        @Override
        public void setChanged() {
            SpiceRackBlockEntity.this.setChanged();
            SpiceRackBlockEntity.this.sync();
        }
    };

    private boolean hasPreservationUpgrade;
    private Component customName;
    private boolean isDirty;

    private final ContainerKitchenItemProvider conservingItemProvider = new ConversingKitchenItemProvider(container);
    private final ContainerKitchenItemProvider containerItemProvider = new ContainerKitchenItemProvider(container);
    private final KitchenItemProvider itemProvider = new ConditionalKitchenItemProvider<>(this::hasPreservationUpgrade, conservingItemProvider, containerItemProvider);

    public SpiceRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.spiceRack.get(), pos, state);
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
    public void loadAdditional(CompoundTag tagCompound, HolderLookup.Provider provider) {
        tagCompound.getCompound("ItemHandler").ifPresent(it -> container.deserialize(it, provider));

        customName = tagCompound.getString("CustomName")
                .map(it -> Component.Serializer.fromJson(it, provider)).orElse(null);

        hasPreservationUpgrade = tagCompound.getBooleanOr("HasPreservationUpgrade", false);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("ItemHandler", container.serialize(provider));
        tag.putBoolean("HasPreservationUpgrade", hasPreservationUpgrade);

        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName, provider));
        }
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag, level.registryAccess());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new SpiceRackMenu(i, playerInventory, this);
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
        return Component.translatable("container.cookingforblockheads.spice_rack");
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SpiceRackBlockEntity blockEntity) {
        blockEntity.serverTick(level, pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (isDirty) {
            sync();
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
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(KitchenItemProvider.class, itemProvider));
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (hasPreservationUpgrade()) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5f, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(ModItems.preservationChamber));
        }
    }
}
