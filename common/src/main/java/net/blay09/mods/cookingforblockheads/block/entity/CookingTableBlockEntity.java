package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.BalmMenuProvider;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.cookingforblockheads.crafting.KitchenMultiblockScanner;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenuAccess;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.menu.ServerKitchenMenuState;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;


public class CookingTableBlockEntity extends BlockEntity implements BalmMenuProvider<Unit> {

    private ItemStack noFilterBook = ItemStack.EMPTY;

    public CookingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cookingTable.value(), pos, state);
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
    public void saveAdditional(ValueOutput output) {
        output.store("NoFilterBook", ItemStack.OPTIONAL_CODEC, noFilterBook);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        input.read("NoFilterBook", ItemStack.OPTIONAL_CODEC).ifPresent(this::setNoFilterBook);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cookingforblockheads.cooking_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        final var kitchenScanner = new KitchenMultiblockScanner();
        kitchenScanner.findNeighbourCraftingBlocks(level, worldPosition);
        final var kitchen = kitchenScanner.createKitchen(level, true);
        final var craftingContext = kitchen.createCraftingContext(player);
        final var kitchenMenuAccess = KitchenMenuAccess.create(kitchen, new ServerKitchenMenuState(craftingContext, hasNoFilterBook()));
        return new KitchenMenu(ModMenus.cookingTable.value(), i, player, kitchenMenuAccess);
    }

    @Override
    public Unit getScreenOpeningData(ServerPlayer player) {
        return Unit.INSTANCE;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Unit> getScreenStreamCodec() {
        return Unit.STREAM_CODEC.cast();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (hasNoFilterBook()) {
            ItemUtils.spawnItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, getNoFilterBook());
        }
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return BalmBlockEntityUtils.createUpdatePacket(this);
    }

}
