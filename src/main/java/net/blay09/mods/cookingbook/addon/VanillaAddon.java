package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.api.CookingAPI;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class VanillaAddon {

    public VanillaAddon() {
        CookingAPI.addSinkRecipe(new ItemStack(Items.potionitem), new ItemStack(Items.glass_bottle, 1));
    }

}
