package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void load() {
        GameRegistry.addSmelting(Items.BOOK, new ItemStack(ModItems.recipeBook, 1, 1), 0f);
        GameRegistry.addSmelting(new ItemStack(ModItems.recipeBook, 1, 0), new ItemStack(ModItems.recipeBook, 1, 1), 0f);
    }

}
