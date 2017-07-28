package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;

public class VanillaFoodPantryAddon extends SimpleAddon {
	private static final String[] ADDITIONAL_RECIPES = new String[] {
			"acornmeal_portion",
			"baking_soda",
			"broth_jar",
			"bucket_condensed_milk",
			"fizzing_agent",
			"cloth_bag",
			"condensed_milk_jar",
			"dark_empty_jar",
			"dried_seeds",
			"drying_agent",
			"drying_agent_unprocessed",
			"drying_agent_unprocessed_ball",
			"drying_agent_ball",
			"egg_white",
			"empty_bottle",
			"empty_jar",
			"enzyme_extractor_agent",
			"fermented_feather",
			"fizzing_agent",
			"flesh_meal",
			"flour_portion",
			"fudge_portion",
			"gelatin_ball",
			"jungle_juice_jar",
			"leavening_agent",
			"leavening_agent_ball",
			"luminesce_jar",
			"molasses_ball",
			"portion_mayonnaise",
			"portion_butter",
			"portion_salt",
			"portion_sugar",
			"raw_caffeine",
			"rennet",
			"sand_jar",
			"seedmush",
			"seedoil_jar",
			"small_bone",
			"sticky_paste",
			"salt",
			"skewers_wood",
	};

	private static final String[] TOOLS = new String[] {
			"bit_pipette",
			"fermenting_bucket",
			"flint_cutter",
			"whisk",
			"weighted_plate"
	};

	private static final String FRESH_WATER_ITEM = "water_portion";
	private static final String FRESH_MILK_ITEM = "milk_portion";


	public VanillaFoodPantryAddon() {
		super("vanillafoodpantry");

		addNonFoodRecipe(ADDITIONAL_RECIPES);
		addTool(TOOLS);

		CookingForBlockheadsAPI.addWaterItem(getModItemStack(FRESH_WATER_ITEM));
		CookingForBlockheadsAPI.addMilkItem(getModItemStack(FRESH_MILK_ITEM));
	}
}