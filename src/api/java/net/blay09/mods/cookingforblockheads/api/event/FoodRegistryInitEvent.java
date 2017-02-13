package net.blay09.mods.cookingforblockheads.api.event;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collection;
import java.util.List;

public class FoodRegistryInitEvent extends Event {

    private final List<ItemStack> nonFoodRecipes = Lists.newArrayList();

    public void registerNonFoodRecipe(ItemStack result) {
        nonFoodRecipes.add(result);
    }

    public Collection<ItemStack> getNonFoodRecipes() {
        return nonFoodRecipes;
    }

}
