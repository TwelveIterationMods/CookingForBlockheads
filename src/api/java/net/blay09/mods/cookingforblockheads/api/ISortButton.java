package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Comparator;

public interface ISortButton {
    ResourceLocation getIcon();

    String getTooltip();

    Comparator<FoodRecipeWithStatus> getComparator(PlayerEntity player);

    int getIconTextureX();

    int getIconTextureY();
}
