package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.List;

public class TileCuttingBoard extends TileEntity {

    private final IKitchenItemProvider itemProvider = new DefaultKitchenItemProvider() {
        private final ItemStack cuttingBoard = Compat.cuttingBoardItem != Items.AIR ? new ItemStack(Compat.cuttingBoardItem) : ItemStack.EMPTY;

        @Override
        public ItemStack returnItemStack(ItemStack itemStack) {
            if (itemStack.getItem() == Compat.cuttingBoardItem) {
                return ItemStack.EMPTY;
            }

            return itemStack;
        }

        @Nullable
        @Override
        public SourceItem findSourceAndMarkAsUsed(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
            if (predicate.test(cuttingBoard, 1)) {
                return new SourceItem(this, -1, cuttingBoard);
            }

            return super.findSourceAndMarkAsUsed(predicate, maxAmount, inventories, requireBucket, simulate);
        }
    };

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityKitchenItemProvider.CAPABILITY
                || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityKitchenItemProvider.CAPABILITY) {
            return CapabilityKitchenItemProvider.CAPABILITY.cast(itemProvider);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

}