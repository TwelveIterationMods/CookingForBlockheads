package net.blay09.mods.cookingbook;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class SinkRecipes {

    private static final Map<String, ItemStack> recipes = new HashMap<String, ItemStack>();

    public static void addSinkRecipe(ItemStack itemStackIn, ItemStack itemStackOut) {
        recipes.put(itemStackIn.getUnlocalizedName() + "@" + itemStackIn.getItemDamage(), itemStackOut);
    }

    public static ItemStack getSinkOutput(ItemStack itemStack) {
        if(itemStack == null) {
            return null;
        }
        ItemStack resultStack = recipes.get(itemStack.getUnlocalizedName() + "@" + itemStack.getItemDamage());
        if(resultStack == null) {
            resultStack = recipes.get(itemStack.getUnlocalizedName() + "@" + OreDictionary.WILDCARD_VALUE);
        }
        return resultStack;
    }

}
