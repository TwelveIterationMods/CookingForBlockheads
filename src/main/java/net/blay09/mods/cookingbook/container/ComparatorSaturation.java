package net.blay09.mods.cookingbook.container;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class ComparatorSaturation implements Comparator<ItemStack> {

    private final ComparatorName fallback = new ComparatorName();

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        boolean isFirstFood = o1.getItem() instanceof ItemFood;
        boolean isSecondFood = o2.getItem() instanceof ItemFood;
        if(!isFirstFood && !isSecondFood) {
            return 0;
        } else if(!isFirstFood) {
            return 1;
        } else if(!isSecondFood) {
            return -1;
        }
        ItemFood f1 = (ItemFood) o1.getItem();
        ItemFood f2 = (ItemFood) o2.getItem();
        int result = (int) (f2.func_150906_h(o2) * 100 - f1.func_150906_h(o1) * 100);
        if(result == 0) {
            return fallback.compare(o1, o2);
        }
        return result;
    }

}
