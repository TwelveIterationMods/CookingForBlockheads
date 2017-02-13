package net.blay09.mods.cookingforblockheads.api;

import java.util.Comparator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public interface ISortButton {
	ResourceLocation getIcon();
	String getTooltip();
	Comparator<FoodRecipeWithStatus> getComparator(EntityPlayer player);
	int getIconTextureX();
	int getIconTextureY();
}
