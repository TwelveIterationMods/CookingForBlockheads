package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

	public static void load(Configuration config) {
		boolean noFilterEdition = config.getBoolean("NoFilter Edition", "items", true, "");
		boolean craftingEdition = config.getBoolean("Cooking for Blockheads II", "items", true, "");

		// #NoFilter Edition
		if(noFilterEdition) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.recipeBook, 1, 0), new ItemStack(ModItems.recipeBook, 1, 1));
		}

		// Cooking for Blockheads I
		if(config.getBoolean("Cooking for Blockheads I", "items", true, "")) {
			GameRegistry.addSmelting(Items.BOOK, new ItemStack(ModItems.recipeBook, 1, 1), 0f);
			if (noFilterEdition) {
				GameRegistry.addShapelessRecipe(new ItemStack(ModItems.recipeBook, 1, 1), new ItemStack(ModItems.recipeBook, 1, 0));
			}
		}

		// Cooking for Blockheads II
		if(craftingEdition) {
			GameRegistry.addRecipe(new ItemStack(ModItems.recipeBook, 1, 2), " D ", "CBC", " D ", 'C', Blocks.CRAFTING_TABLE, 'D', Items.DIAMOND, 'B', new ItemStack(ModItems.recipeBook, 1, 1));
			GameRegistry.addRecipe(new ItemStack(ModItems.recipeBook, 1, 2), " C ", "DBD", " C ", 'C', Blocks.CRAFTING_TABLE, 'D', Items.DIAMOND, 'B', new ItemStack(ModItems.recipeBook, 1, 1));
		}

		// Fridge
		if(config.getBoolean("Fridge", "blocks", true, "")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.fridge), Blocks.CHEST, Items.IRON_DOOR);
		}

		// Sink
		if(config.getBoolean("Sink", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.sink), "III", "WBW", "WWW", 'I', "ingotIron", 'W', "logWood", 'B', Items.WATER_BUCKET));
		}

		// Toaster
		if(config.getBoolean("Toaster", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.toaster), "I I", "C C", "III", 'I', "ingotIron", 'C', Items.COAL));
		}

		// Spice Rack
		if(config.getBoolean("Spice Rack", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.spiceRack), "slabWood"));
		}

		// Milk Jar
		if(config.getBoolean("Milk Jar", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.milkJar), "GPG", "GMG", "GGG", 'G', "blockGlass", 'P', "plankWood", 'M', Items.MILK_BUCKET));
		}

		// Cooking Table
		if(config.getBoolean("Cooking Table", "blocks", true, "")) {
			if(craftingEdition) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cookingTable), "CCC", "WBW", "WWW", 'B', new ItemStack(ModItems.recipeBook, 1, 2), 'W', "logWood", 'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15)));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cookingTable), "CCC", "WBW", "WWW", 'B', Items.BOOK, 'W', "logWood", 'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15)));
			}
		}

		// Cooking Oven
		if(config.getBoolean("Cooking Oven", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.oven), "GGG", "IFI", "III", 'G', new ItemStack(Blocks.STAINED_GLASS, 1, 15), 'I', "ingotIron", 'F', Blocks.FURNACE));
		}

		// Tool Rack
		if(config.getBoolean("Tool Rack", "blocks", true, "")) {
			if(OreDictionary.getOres("nuggetIron", false).size() > 0) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.toolRack), "SSS", "I I", 'S', "slabWood", 'I', "nuggetIron"));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.toolRack), "SSS", "I I", 'S', "slabWood", 'I', Blocks.STONE_BUTTON));
			}
		}
	}

}
