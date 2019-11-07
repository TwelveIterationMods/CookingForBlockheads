package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.container.ModContainers;
import net.blay09.mods.cookingforblockheads.container.RecipeBookContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CookingTableTileEntity extends TileEntity implements INamedContainerProvider {

    @SuppressWarnings("NullableProblems")
    private final LazyOptional<CapabilityKitchenConnector.IKitchenConnector> kitchenConnectorCap = LazyOptional.of(CapabilityKitchenConnector.CAPABILITY::getDefaultInstance);

    private ItemStack noFilterBook = ItemStack.EMPTY;

    public CookingTableTileEntity() {
        super(ModTileEntities.cookingTable);
    }

    public boolean hasNoFilterBook() {
        return !noFilterBook.isEmpty();
    }

    public ItemStack getNoFilterBook() {
        return noFilterBook;
    }

    public void setNoFilterBook(ItemStack noFilterBook) {
        this.noFilterBook = noFilterBook;
        markDirty();
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        super.write(tagCompound);
        CompoundNBT itemCompound = new CompoundNBT();
        if (!noFilterBook.isEmpty()) {
            noFilterBook.write(itemCompound);
        }

        tagCompound.put("NoFilterBook", itemCompound);
        return tagCompound;
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        if (tagCompound.contains("NoFilterBook")) {
            setNoFilterBook(ItemStack.read(tagCompound.getCompound("NoFilterBook")));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityKitchenConnector.CAPABILITY.orEmpty(cap, kitchenConnectorCap);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cookingforblockheads.cooking_table");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        RecipeBookContainer container = new RecipeBookContainer(ModContainers.cookingTable, i, playerEntity).allowCrafting();
        if (!noFilterBook.isEmpty()) {
            container.setNoFilter();
        }
        container.setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(world, pos));
        return container;
    }
}
