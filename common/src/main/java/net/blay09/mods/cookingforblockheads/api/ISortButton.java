package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

public interface ISortButton {
    ResourceLocation getIcon();

    String getTooltip();

    Comparator<FoodRecipeWithStatus> getComparator(Player player);

    int getIconTextureX();

    int getIconTextureY();
}
