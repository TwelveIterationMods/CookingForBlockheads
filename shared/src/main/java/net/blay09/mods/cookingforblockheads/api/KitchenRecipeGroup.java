package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface KitchenRecipeGroup {
    Item getParentItem();
    List<Ingredient> getChildren();
}
