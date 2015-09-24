package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.CookingBook;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenItemProvider;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySink extends TileEntity implements IKitchenItemProvider {

    private static class WaterTank extends FluidTank {
        public WaterTank(int capacity) {
            super(capacity);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if(resource.getFluid() != FluidRegistry.WATER) {
                return 0;
            }
            return super.fill(resource, doFill);
        }
    }

    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final FluidTank waterTank = new WaterTank(16000);
    private int craftingBuffer;

    public TileEntitySink() {
        itemStacks.add(new ItemStack(Items.water_bucket));
        ItemStack pamsWater = GameRegistry.findItemStack("harvestcraft", "freshwaterItem", 1);
        if(pamsWater != null) {
            itemStacks.add(pamsWater);
        }
    }

    @Override
    public List<ItemStack> getProvidedItemStacks() {
        return itemStacks;
    }

    @Override
    public boolean addToCraftingBuffer(ItemStack itemStack) {
        if(!CookingBook.sinkRequiresWater) {
            return true;
        }
        if(waterTank.getFluidAmount() < 1000) {
            return false;
        }
        craftingBuffer += 1000;
        return true;
    }

    @Override
    public void clearCraftingBuffer() {
        craftingBuffer = 0;
    }

    @Override
    public void craftingComplete() {
        waterTank.drain(craftingBuffer, true);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        waterTank.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        waterTank.readFromNBT(tagCompound);
    }
}
