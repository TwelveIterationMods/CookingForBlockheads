package net.blay09.mods.cookingforblockheads.api;

import java.util.Comparator;

import net.minecraft.util.ResourceLocation;

public interface ICustomSortButton {

	ResourceLocation getIcon();
	String getTooltip();
	Comparator<FoodRecipeWithStatus> getComparator();
	int getIconTextureX();
	int getIconTextureY();
	
}
