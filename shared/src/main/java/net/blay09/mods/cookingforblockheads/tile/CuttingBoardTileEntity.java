package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CuttingBoardTileEntity extends TileEntity {

    private final IKitchenItemProvider itemProvider = new DefaultKitchenItemProvider() {
        private final ItemStack cuttingBoard = Compat.cuttingBoardItem != Items.AIR ? new ItemStack(Compat.cuttingBoardItem) : ItemStack.EMPTY;

        @Override
        public ItemStack returnItemStack(ItemStack itemStack, SourceItem sourceItem) {
            if (itemStack.getItem() == Compat.cuttingBoardItem) {
                return ItemStack.EMPTY;
            }

            return itemStack;
        }

        @Nullable
        @Override
        public SourceItem findSource(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
            if (predicate.test(cuttingBoard, 1)) {
                return new SourceItem(this, -1, cuttingBoard);
            }

            return super.findSource(predicate, maxAmount, inventories, requireBucket, simulate);
        }
    };

    private final LazyOptional<IKitchenItemProvider> itemProviderCap = LazyOptional.of(() -> itemProvider);

    public CuttingBoardTileEntity() {
        super(ModTileEntities.cuttingBoard);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);

        if (result.isPresent()) {
            return result;
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
