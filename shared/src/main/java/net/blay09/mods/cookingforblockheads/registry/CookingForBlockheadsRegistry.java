package net.blay09.mods.cookingforblockheads.registry;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.api.KitchenRecipeHandler;
import net.blay09.mods.cookingforblockheads.client.gui.NameSortButton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CookingForBlockheadsRegistry {

    private static final List<ISortButton> sortButtons = new ArrayList<>();

    public static void initialize(BalmEvents events) {
        Balm.getEvents().onEvent(RecipesUpdatedEvent.class, event -> {
            CookingRegistry.initFoodRegistry(event.getRecipeManager(), event.getRegistryAccess());
        });

        Balm.getEvents()
                .onEvent(ServerReloadFinishedEvent.class,
                        (ServerReloadFinishedEvent event) -> CookingRegistry.initFoodRegistry(event.getServer().getRecipeManager(),
                                event.getServer().registryAccess()));

        Balm.getEvents().onEvent(ServerStartedEvent.class, event -> {
            RecipeManager recipeManager = event.getServer().getRecipeManager();
            CookingRegistry.initFoodRegistry(recipeManager, event.getServer().registryAccess());
        });
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
// TODO
    }
}
