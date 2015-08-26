package net.blay09.mods.cookingbook.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FoodRegistryInitEvent extends Event {

    private final List<ItemStack> nonFoodRecipes = new ArrayList<>();

    public void registerNonFoodRecipe(ItemStack result) {
        nonFoodRecipes.add(result);
    }

    public Collection<ItemStack> getNonFoodRecipes() {
        return nonFoodRecipes;
    }
}
