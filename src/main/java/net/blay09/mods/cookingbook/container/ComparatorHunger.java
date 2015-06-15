package net.blay09.mods.cookingbook.container;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class ComparatorHunger implements Comparator<ItemStack> {

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        if(!(o1.getItem() instanceof ItemFood)) {
            return 1;
        } else if(!(o2.getItem() instanceof ItemFood)) {
            return -1;
        }
        ItemFood f1 = (ItemFood) o1.getItem();
        ItemFood f2 = (ItemFood) o2.getItem();
        return f2.getHealAmount(o2) - f1.getHealAmount(o1);
    }

}
