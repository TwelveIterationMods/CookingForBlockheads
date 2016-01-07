package net.blay09.mods.cookingbook;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class CookingConfig {

    public static boolean sinkRequiresWater;
    public static boolean ovenRequiresCookingOil;
    public static boolean disallowOvenAutomation;
    public static float ovenFuelTimeMultiplier;
    public static float ovenCookTimeMultiplier;

    public static boolean enableSink;
    public static boolean enableOven;
    public static boolean enableToaster;

    public static boolean enableCraftingBook;
    public static boolean enableNoFilter;

    public static boolean moduleVanilla;
    public static boolean moduleHarvestCraft;
    public static boolean moduleEnviroMine;
    public static boolean moduleAppleCore;
    public static boolean usePamsToast;

    public static boolean disableItemRender;

    public static void load(File configFile) {
        Configuration config = new Configuration(configFile);
        sinkRequiresWater = config.getBoolean("sinkRequiresWater", "general", false, "Set this to true if you'd like the sink to require water to be piped in, instead of providing infinite of it.");
        ovenRequiresCookingOil = config.getBoolean("ovenRequiresCookingOil", "general", false, "Set this to true if you'd like the oven to only accept cooking oil as fuel (requires Pam's Harvestcraft)");
        disallowOvenAutomation = config.getBoolean("disallowOvenAutomation", "general", false, "Set this to true if you'd like to disallow automation of the oven (pipes and such won't be able to insert/extract)");
        ovenFuelTimeMultiplier = config.getFloat("ovenFuelTimeMultiplier", "general", 0.33f, 0.1f, 2f, "The fuel multiplier for the cooking oven. Higher values means fuel lasts longer, 1.0 is furnace default.");
        ovenCookTimeMultiplier = config.getFloat("ovenCookTimeMultiplier", "general", 1f, 0.25f, 9f, "The cooking time multiplier for the cooking oven. Higher values means it will take longer.");

        config.setCategoryComment("blocks", "Setting any of these options to false will disable their crafting recipe.");
        enableSink = config.getBoolean("Sink", "blocks", true, "");
        enableOven = config.getBoolean("Cooking Oven", "blocks", true, "");
        enableToaster = config.getBoolean("Toaster", "blocks", true, "");

        config.setCategoryComment("items", "Setting any of these options to false will disable their crafting recipe.");
        enableNoFilter = config.getBoolean("#NoFilter Edition", "items", true, "");
        enableCraftingBook = config.getBoolean("Crafting Book", "items", true, "Disabling this will also disable the Cooking Table recipe.");

        config.setCategoryComment("modules", "Setting any of these options to false will disable their specific mod support.");
        moduleVanilla = config.getBoolean("Vanilla Minecraft", "modules", true, "Sink Support, Ingredient Recipes");
        moduleHarvestCraft = config.getBoolean("Pam's HarvestCraft", "modules", true, "Multiblock Kitchen Support, Tool Support, Oven Recipes, Oven Fuel, Ingredient Recipes");
        moduleEnviroMine = config.getBoolean("EnviroMine", "modules", true, "Multiblock Kitchen Support (Freezer)");
        moduleAppleCore = config.getBoolean("AppleCore", "modules", true, "Dynamic Food Values");
        usePamsToast = config.getBoolean("Use Pam's Toast", "modules", true, "Should bread be toasted into Pam's toast (if available) instead of the Cooking for Blockheads one?");

        disableItemRender = config.getBoolean("disableItemRender", "client", false, "If you hate cool things, set this to true to disable the item rendering inside of fridges and ovens. Note that the inside only renders when the door is open anyways, so you won't gain much from this.");

        config.save();
    }

}
