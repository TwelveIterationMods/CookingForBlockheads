package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.minecraft.item.ItemStack;

public class ExtraFoodAddon extends SimpleAddon {

	public ExtraFoodAddon() {
		super("extrafood");

		addTool("Knife",
				"Grater",
				"MuffinPan");

		addNonFoodRecipe(
				"dough",
				"ChocolateCake");

		final ItemStack breadItem = getModItemStack("BreadSlice");
		final ItemStack toastItem = getModItemStack("Toast");
		if(breadItem != null && toastItem != null) {
			CookingForBlockheadsAPI.addToastHandler(breadItem, new ToastOutputHandler() {
				@Override
				public ItemStack getToasterOutput(ItemStack itemStack) {
					return toastItem;
				}
			});
		}
	}

}
