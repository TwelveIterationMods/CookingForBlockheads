package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.util.TransferableBlockEntity;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class CookingTableBlockEntity extends BalmBlockEntity implements BalmMenuProvider<BlockPos>, TransferableBlockEntity<ItemStack> {

    private ItemStack noFilterBook = ItemStack.EMPTY;

    public CookingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cookingTable.get(), pos, state);
    }

    public boolean hasNoFilterBook() {
        return !noFilterBook.isEmpty();
    }

    public ItemStack getNoFilterBook() {
        return noFilterBook;
    }

    public void setNoFilterBook(ItemStack noFilterBook) {
        this.noFilterBook = noFilterBook;
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag itemCompound = new CompoundTag();
        if (!noFilterBook.isEmpty()) {
            noFilterBook.save(provider, itemCompound);
        }

        tag.put("NoFilterBook", itemCompound);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        if (tag.contains("NoFilterBook")) {
            setNoFilterBook(ItemStack.parseOptional(provider, tag.getCompound("NoFilterBook")));
        }
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag, level.registryAccess());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cookingforblockheads.cooking_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new KitchenMenu(ModMenus.cookingTable.get(), i, player, new KitchenImpl(level, worldPosition));
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return worldPosition;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public ItemStack snapshotDataForTransfer() {
        final var snapshot = noFilterBook;
        noFilterBook = ItemStack.EMPTY;
        return snapshot;
    }

    @Override
    public void restoreFromTransferSnapshot(ItemStack data) {
        setNoFilterBook(data);
    }
}
