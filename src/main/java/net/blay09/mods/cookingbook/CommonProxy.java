package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.addon.VanillaAddon;
import net.blay09.mods.cookingbook.block.*;
import net.blay09.mods.cookingbook.item.*;
import net.blay09.mods.cookingbook.network.NetworkHandler;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {}

	public void init(FMLInitializationEvent event) {
		GameRegistry.registerItem(CookingBook.itemRecipeBook, "recipebook");
		GameRegistry.registerBlock(CookingBook.blockCookingTable, ItemBlockCookingTable.class, "cookingtable");
		GameRegistry.registerBlock(CookingBook.blockCookingOven, ItemBlockCookingOven.class, "cookingoven");
		GameRegistry.registerBlock(CookingBook.blockFridge, ItemBlockFridge.class, "fridge");
		GameRegistry.registerBlock(CookingBook.blockSink, ItemBlockSink.class, "sink");
		GameRegistry.registerBlock(CookingBook.blockToolRack, ItemBlockToolRack.class, "toolrack");
		GameRegistry.registerTileEntity(TileEntityCookingOven.class, "cookingbook:cookingoven");
		GameRegistry.registerTileEntity(TileEntityFridge.class, "cookingbook:fridge");
		GameRegistry.registerTileEntity(TileEntityToolRack.class, "cookingbook:toolrack");
		GameRegistry.registerTileEntity(TileEntitySink.class, "cookingbook:sink");
		GameRegistry.registerTileEntity(TileEntityCookingTable.class, "cookingbook:cookingtable");

		// #NoFilter Edition
		GameRegistry.addShapelessRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 3), Items.book, Items.painting);
		// Cooking for Blockheads I
		GameRegistry.addSmelting(Items.book, new ItemStack(CookingBook.itemRecipeBook), 0f);
		// Cooking for Blockheads II
		GameRegistry.addRecipe(new ItemStack(CookingBook.itemRecipeBook, 1, 1), " C ", "DBD", " C ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'B', CookingBook.itemRecipeBook);
		// Fridge
		GameRegistry.addShapelessRecipe(new ItemStack(CookingBook.blockFridge), Blocks.chest, Items.iron_door);
		// Sink
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CookingBook.blockSink), "III", "WBW", "WWW", 'I', "ingotIron", 'W', "logWood", 'B', Items.water_bucket));
		// Cooking Table
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CookingBook.blockCookingTable), "CCC", "WBW", "WWW", 'B', new ItemStack(CookingBook.itemRecipeBook, 1, 1), 'W', "logWood", 'C', new ItemStack(Blocks.stained_hardened_clay, 1, 15)));
		// Cooking Oven
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CookingBook.blockCookingOven), "GGG", "IFI", "III", 'G', new ItemStack(Blocks.stained_glass, 1, 15), 'I', "ingotIron", 'F', Blocks.furnace));
		// Tool Rack
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CookingBook.blockToolRack), "PPP", "I I", 'P', Blocks.wooden_pressure_plate, 'I', "ingotIron"));

		NetworkHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(CookingBook.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		new VanillaAddon();
		//event.buildSoftDependProxy("MineTweaker3", "net.blay09.mods.cookingbook.addon.MineTweakerAddon");
		event.buildSoftDependProxy("harvestcraft", "net.blay09.mods.cookingbook.addon.HarvestCraftAddon");

		CookingRegistry.initFoodRegistry();
	}
}
