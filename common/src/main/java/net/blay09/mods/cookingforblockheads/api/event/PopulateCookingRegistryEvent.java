package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.Balmstrap;
import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.function.Consumer;

public record PopulateCookingRegistryEvent(RecipeManager recipeManager) {

    public static BidirectionalEventMapper<Consumer<PopulateCookingRegistryEvent>> EVENT = Balmstrap.createBoundCustomEvent(PopulateCookingRegistryEvent.class);

}
