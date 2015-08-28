package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class OvenFuels {

    private static final Map<String, Integer> fuelTimes = new HashMap<>();

    public static void addOvenFuel(ItemStack itemStack, int fuelTime) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        if(identifier != null) {
            String key = identifier.name;
            if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                key += "@*";
            } else {
                key += "@" + itemStack.getItemDamage();
            }
            fuelTimes.put(key, fuelTime);
        }
    }

    public static int getFuelTime(ItemStack itemStack) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        if(identifier != null) {
            String key = identifier.name + "@" + itemStack.getItemDamage();
            if(fuelTimes.containsKey(key)) {
                return fuelTimes.get(key);
            } else {
                key = identifier.name + "@*";
                if(fuelTimes.containsKey(key)) {
                    return fuelTimes.get(key);
                }
            }
        }
        return 0;
    }

}
