package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.menu.ModMenus;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    public CompoundTag save(CompoundTag tagCompound) {
        super.save(tagCompound);
        CompoundTag itemCompound = new CompoundTag();
        if (!noFilterBook.isEmpty()) {
            noFilterBook.save(itemCompound);
        }

        tagCompound.put("NoFilterBook", itemCompound);
        return tagCompound;
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        if (tagCompound.contains("NoFilterBook")) {
            setNoFilterBook(ItemStack.of(tagCompound.getCompound("NoFilterBook")));
        }
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        return save(tag);
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenConnector.class, new DefaultKitchenConnector()));
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.cookingforblockheads.cooking_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        RecipeBookMenu container = new RecipeBookMenu(ModMenus.cookingTable.get(), i, playerEntity).allowCrafting();
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
