package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.api.CookingAPI;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class VanillaAddon {

    public VanillaAddon() {
        CookingAPI.addSinkRecipe(new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.glass_bottle, 1));
        CookingAPI.addSinkRecipe(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.wool, 1));
        CookingAPI.addSinkRecipe(new ItemStack(Blocks.carpet, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.carpet, 1));
    }

}
