package net.blay09.mods.cookingforblockheads.api.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collection;

public class FoodRegistryInitEvent extends Event {

    private final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();

    public void registerNonFoodRecipe(ItemStack result) {
        nonFoodRecipes.add(result);
    }

    public Collection<ItemStack> getNonFoodRecipes() {
        return nonFoodRecipes;
    }

}
