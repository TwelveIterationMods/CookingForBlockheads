package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {}

	public void init(FMLInitializationEvent event) {
		GameRegistry.registerItem(CookingBook.itemRecipeBook, "recipebook");

		CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.itemRecipeBook), " B ", "SCS", " B ", 'B', Items.bread, 'S', Items.wheat_seeds, 'C', Items.book);

		NetworkRegistry.INSTANCE.registerGuiHandler(CookingBook.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		FoodRegistry.init();
	}
}
