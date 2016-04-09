package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	public static void init(Configuration config) {
		boolean noFilterEdition = config.getBoolean("NoFilter Edition", "items", true, "");
		boolean craftingEdition = config.getBoolean("Cooking for Blockheads II", "items", true, "");

		// #NoFilter Edition
		if(noFilterEdition) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.recipeBook, 1, 0), new ItemStack(ModItems.recipeBook, 1, 1));
		}

		// Cooking for Blockheads I
		if(config.getBoolean("Cooking for Blockheads I", "items", true, "")) {
			GameRegistry.addSmelting(Items.book, new ItemStack(ModItems.recipeBook, 1, 1), 0f);
			if (noFilterEdition) {
				GameRegistry.addShapelessRecipe(new ItemStack(ModItems.recipeBook, 1, 1), new ItemStack(ModItems.recipeBook, 1, 0));
			}
		}

		// Cooking for Blockheads II
		if(craftingEdition) {
			GameRegistry.addRecipe(new ItemStack(ModItems.recipeBook, 1, 2), " D ", "CBC", " D ", 'C', Blocks.crafting_table, 'D', Items.diamond, 'B', new ItemStack(ModItems.recipeBook, 1, 1));
		}

		// Fridge
		if(config.getBoolean("Fridge", "blocks", true, "")) {
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.fridge), Blocks.chest, Items.iron_door);
		}

		// Sink
		if(config.getBoolean("Sink", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.sink), "III", "WBW", "WWW", 'I', "ingotIron", 'W', "logWood", 'B', Items.water_bucket));
		}

		// Toaster
		if(config.getBoolean("Toaster", "blocks", true, "")) {
			//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CookingForBlockheads.sink), "III", "WBW", "WWW", 'I', "ingotIron", 'W', "logWood", 'B', Items.water_bucket));
		}

		// Cooking Table
		if(config.getBoolean("Cooking Table", "blocks", true, "")) {
			if(craftingEdition) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cookingTable), "CCC", "WBW", "WWW", 'B', new ItemStack(ModItems.recipeBook, 1, 2), 'W', "logWood", 'C', new ItemStack(Blocks.stained_hardened_clay, 1, 15)));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cookingTable), "CCC", "WBW", "WWW", 'B', Items.book, 'W', "logWood", 'C', new ItemStack(Blocks.stained_hardened_clay, 1, 15)));
			}
		}

		// Cooking Oven
		if(config.getBoolean("Cooking Oven", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.oven), "GGG", "IFI", "III", 'G', new ItemStack(Blocks.stained_glass, 1, 15), 'I', "ingotIron", 'F', Blocks.furnace));
		}

		// Tool Rack
		if(config.getBoolean("Tool Rack", "blocks", true, "")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.toolRack), "SSS", "I I", 'S', "slabWood", 'I', "nuggetIron"));
		}
	}

}
