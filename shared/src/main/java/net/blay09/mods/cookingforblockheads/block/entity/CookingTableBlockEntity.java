package net.blay09.mods.cookingforblockheads.block.entity;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class CookingTableBlockEntity extends BalmBlockEntity implements BalmMenuProvider {

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
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        CompoundTag itemCompound = new CompoundTag();
        if (!noFilterBook.isEmpty()) {
            noFilterBook.save(itemCompound);
        }

        tag.put("NoFilterBook", itemCompound);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("NoFilterBook")) {
            setNoFilterBook(ItemStack.of(tag.getCompound("NoFilterBook")));
        }
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenConnector.class, new DefaultKitchenConnector()));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cookingforblockheads.cooking_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        RecipeBookMenu container = new RecipeBookMenu(ModMenus.cookingTable.get(), i, player).allowCrafting();
        if (!noFilterBook.isEmpty()) {
            container.setNoFilter();
        }
        container.setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(level, worldPosition));
        return container;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }
}
