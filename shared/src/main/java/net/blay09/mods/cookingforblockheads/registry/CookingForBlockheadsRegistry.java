package net.blay09.mods.cookingforblockheads.registry;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;

public class CookingForBlockheadsRegistry {

    private static final List<ISortButton> sortButtons = new ArrayList<>();
    private static final Map<ItemStack, Integer> ovenFuelItems = new HashMap<>();

    public static void initialize(BalmEvents events) {
        events.onEvent(RecipesUpdatedEvent.class, event -> gatherFoodRecipes(event.getRecipeManager(), event.getRegistryAccess()));

        events.onEvent(ServerReloadFinishedEvent.class,
                (ServerReloadFinishedEvent event) -> gatherFoodRecipes(event.getServer().getRecipeManager(), event.getServer().registryAccess()));

        events.onEvent(ServerStartedEvent.class, event -> gatherFoodRecipes(event.getServer().getRecipeManager(), event.getServer().registryAccess()));
    }

    private static void gatherFoodRecipes(RecipeManager recipeManager, RegistryAccess registryAccess) {
        // TODO
    }

    public static <T extends Recipe<?>> KitchenRecipeHandler<T> getRecipeWorkshopHandler(T recipe) {
        return null; // TODO
    }

    public static void addSortButton(ISortButton nameSortButton) {
        sortButtons.add(nameSortButton);
    }

    public static Collection<ISortButton> getSortButtons() {
        return sortButtons;
    }

    public static void addOvenFuel(ItemStack fuelItem, int fuelTime) {
        ovenFuelItems.put(fuelItem, fuelTime);
    }

    public static int getOvenFuelTime(ItemStack itemStack) {
        for (final var entry : ovenFuelItems.entrySet()) {
            if (ItemStack.isSameItem(entry.getKey(), itemStack)) {
                return entry.getValue();
            }
        }
        return 0;
    }
}
