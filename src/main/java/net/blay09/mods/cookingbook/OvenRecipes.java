package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class OvenRecipes {

    private static final Map<String, ItemStack> recipes = new HashMap<>();

    public static void addSmeltingItem(ItemStack source, ItemStack result) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(source.getItem());
        if(identifier != null) {
            recipes.put(identifier.name + "@" + (source.getItemDamage() == OreDictionary.WILDCARD_VALUE ? "*" : source.getItemDamage()), result);
        }
    }

    public static ItemStack getSmeltingResult(ItemStack itemStack) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        if(identifier != null) {
            ItemStack result = recipes.get(identifier.name + "@" + itemStack.getItemDamage());
            if(result == null) {
                result = recipes.get(identifier.name + "@*");
            }
            return result;
        }
        return null;
    }

}
