package net.blay09.mods.cookingforblockheads.compat;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class SimpleAddon {

	private final String modId;
	private final List<String> additionalRecipes = Lists.newArrayList();

	public SimpleAddon(String modId) {
		this.modId = modId;

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onFoodRegistryInit(FoodRegistryInitEvent event) {
		for(String s : additionalRecipes) {
			ItemStack itemStack = getModItemStack(s);
			if(!itemStack.isEmpty()) {
				event.registerNonFoodRecipe(itemStack);
			}
		}
	}

	public void addNonFoodRecipe(String... names) {
		Collections.addAll(additionalRecipes, names);
	}

	public void addTool(String... names) {
		for(String name : names) {
			ItemStack toolItem = getModItemStack(name);
			if(!toolItem.isEmpty()) {
				CookingForBlockheadsAPI.addToolItem(toolItem);
			}
		}
	}

	public ItemStack getModItemStack(String name) {
		return getModItemStack(name, 0);
	}

	public ItemStack getModItemStack(String name, int damage) {
		Item item = Item.REGISTRY.getObject(new ResourceLocation(modId, name));
		return item != null ? new ItemStack(item, 1, damage) : ItemStack.EMPTY;
	}
}
