package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.addon.HarvestCraftAddon;
import net.blay09.mods.cookingbook.addon.MineTweakerAddon;
import net.blay09.mods.cookingbook.addon.VanillaAddon;
import net.blay09.mods.cookingbook.block.TileEntityCookingOven;
import net.blay09.mods.cookingbook.block.TileEntityFridge;
import net.blay09.mods.cookingbook.block.TileEntitySink;
import net.blay09.mods.cookingbook.block.TileEntityToolRack;
import net.blay09.mods.cookingbook.food.FoodRegistry;
import net.blay09.mods.cookingbook.item.ItemBlockCookingOven;
import net.blay09.mods.cookingbook.item.ItemBlockCookingTable;
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
		GameRegistry.registerBlock(CookingBook.blockCookingOven, ItemBlockCookingOven.class, "cookingoven");
		GameRegistry.registerBlock(CookingBook.blockFridge, "fridge");
		GameRegistry.registerBlock(CookingBook.blockSink, "sink");
		GameRegistry.registerBlock(CookingBook.blockToolRack, "toolrack");
		GameRegistry.registerTileEntity(TileEntityCookingOven.class, "cookingbook:cookingoven");
		GameRegistry.registerTileEntity(TileEntityFridge.class, "cookingbook:fridge");
		GameRegistry.registerTileEntity(TileEntityToolRack.class, "cookingbook:toolrack");
		GameRegistry.registerTileEntity(TileEntitySink.class, "cookingbook:sink");

		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 3), Items.book, Items.painting);
		FurnaceRecipes.smelting().func_151396_a(Items.book, new ItemStack(CookingBook.itemRecipeBook), 0f);
		if(CookingBook.enableCraftingBook) {
			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 1), " C ", "DBD", " C ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'B', CookingBook.itemRecipeBook);
		}
		if(CookingBook.enableCookingTable) {
			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.blockCookingTable), " B ", " P ", " C ", 'B', new ItemStack(CookingBook.itemRecipeBook, 1, 1), 'P', Blocks.heavy_weighted_pressure_plate, 'C', Blocks.crafting_table);
		}
		if(CookingBook.enableCookingTable) {
			CraftingManager.getInstance().addRecipe(new ItemStack(CookingBook.blockCookingOven), "BFB", "FIF", "BFB", 'B', Blocks.brick_block, 'F', Blocks.furnace, 'I', Blocks.iron_block);
		}

		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingBook.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		new VanillaAddon();
		//event.buildSoftDependProxy("MineTweaker3", "net.blay09.mods.cookingbook.addon.MineTweakerAddon");
		event.buildSoftDependProxy("harvestcraft", "net.blay09.mods.cookingbook.addon.HarvestCraftAddon");

		FoodRegistry.init();
	}
}
