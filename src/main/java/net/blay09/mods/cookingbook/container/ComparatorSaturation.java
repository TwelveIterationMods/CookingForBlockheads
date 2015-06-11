package net.blay09.mods.cookingbook.container;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class ComparatorSaturation implements Comparator<ItemStack> {

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        ItemFood f1 = (ItemFood) o1.getItem();
        ItemFood f2 = (ItemFood) o2.getItem();
        return (int) (f2.getSaturationModifier(o2) * 100 - f1.getSaturationModifier(o1) * 100);
    }

}
