package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class FoodRegistryInitEvent extends BalmEvent {

    private final NonNullList<ItemStack> nonFoodRecipes = NonNullList.create();

    public void registerNonFoodRecipe(ItemStack result) {
        nonFoodRecipes.add(result);
    }

    public Collection<ItemStack> getNonFoodRecipes() {
        return nonFoodRecipes;
    }

}
