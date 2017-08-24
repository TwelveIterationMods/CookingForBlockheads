package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActuallyAdditionsAddon extends SimpleAddon {

	public ActuallyAdditionsAddon() {
		super(Compat.ACTUALLY_ADDITIONS);
		addTool("item_knife");
	}

	@SubscribeEvent
	public void onFoodRegistryInitDough(FoodRegistryInitEvent event) {
		ItemStack dough = getModItemStack("item_misc", 4);
		if(!dough.isEmpty()) {
			event.registerNonFoodRecipe(dough);
		}
		ItemStack riceDough = getModItemStack("item_misc", 9);
		if(!riceDough.isEmpty()) {
			event.registerNonFoodRecipe(riceDough);
		}
	}

}
