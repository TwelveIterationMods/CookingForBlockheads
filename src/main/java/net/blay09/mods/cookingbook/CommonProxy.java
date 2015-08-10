package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.network.NetworkHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {}

	public void init(FMLInitializationEvent event) {
		GameRegistry.registerItem(CookingBook.itemRecipeBook, "recipebook");
		GameRegistry.registerBlock(CookingBook.blockCookingTable, ItemBlockCookingTable.class, "cookingtable");

		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(CookingBook.itemRecipeBook, 3), Items.book, Items.apple);
		FurnaceRecipes.instance().addSmelting(Items.book, new ItemStack(CookingBook.itemRecipeBook), 0f);
		if(CookingBook.enableCraftingBook) {
			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 1), " C ", "DBD", " C ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'B', CookingBook.itemRecipeBook);
		}
		if(CookingBook.enableSmeltingBook) {
//			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 2), " C ", "DBD", " C ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'B', CookingBook.itemRecipeBook);
		}
		if(CookingBook.enableCookingTable) {
			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.blockCookingTable), " B ", " P ", " C ", 'B', new ItemStack(CookingBook.itemRecipeBook, 1, 1), 'P', Blocks.heavy_weighted_pressure_plate, 'C', Blocks.crafting_table);
		}

		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingBook.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		FoodRegistry.init();
	}
}
