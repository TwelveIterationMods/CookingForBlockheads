package net.blay09.mods.cookingbook.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.api.IKitchenItemProvider;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySink extends TileEntity implements IKitchenItemProvider {

    private final List<ItemStack> itemStacks = new ArrayList<>();

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

}
