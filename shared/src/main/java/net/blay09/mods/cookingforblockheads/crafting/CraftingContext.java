package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.api.Kitchen;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CraftingContext {

    private final List<KitchenItemProvider> itemProviders;
    private final List<KitchenItemProcessor> itemProcessors;

    public CraftingContext(final Kitchen kitchen, final @Nullable Player player) {
        itemProviders = kitchen.getItemProviders(player);
        itemProcessors = kitchen.getItemProcessors();
    }

    public CraftingOperation createOperation(RecipeHolder<Recipe<?>> recipe) {
        return new CraftingOperation(this, recipe);
    }

    public List<KitchenItemProvider> getItemProviders() {
        return itemProviders;
    }

    public List<KitchenItemProcessor> getItemProcessors() {
        return itemProcessors;
    }
}
