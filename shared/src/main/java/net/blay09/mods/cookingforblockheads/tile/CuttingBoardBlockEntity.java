package net.blay09.mods.cookingforblockheads.tile;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntity;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.AbstractKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CuttingBoardBlockEntity extends BalmBlockEntity {

    private final IKitchenItemProvider itemProvider = new AbstractKitchenItemProvider() {
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

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cuttingBoard.get(), pos, state);
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider<>(IKitchenItemProvider.class, itemProvider));
    }

}
