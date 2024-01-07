package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

public interface ISortButton {
    ResourceLocation getIcon();

    String getTooltip();

    Comparator<RecipeWithStatus> getComparator(Player player);

    int getIconTextureX();

    int getIconTextureY();
}
