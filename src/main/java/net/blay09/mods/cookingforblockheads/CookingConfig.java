package net.blay09.mods.cookingforblockheads;

import net.minecraftforge.common.config.Configuration;

public class CookingConfig {

    public static boolean sinkRequiresWater;
    public static boolean disallowOvenAutomation;
    public static float ovenFuelTimeMultiplier;
    public static float ovenCookTimeMultiplier;

    public static boolean ovenRequiresCookingOil;

    public static boolean cowJarEnabled;
    public static float cowJarMilkPerTick;

    public static void load(Configuration config) {
        sinkRequiresWater = config.getBoolean("Sink Requires Water", "general", false, "Set this to true if you'd like the sink to require water to be piped in, instead of providing infinite of it.");
        disallowOvenAutomation = config.getBoolean("Disallow Oven Automation", "general", false, "Set this to true if you'd like to disallow automation of the oven (pipes and such won't be able to insert/extract)");
        ovenFuelTimeMultiplier = config.getFloat("Oven Fuel Time Multiplier", "general", 0.33f, 0.1f, 2f, "The fuel multiplier for the cooking oven. Higher values means fuel lasts longer, 1.0 is furnace default.");
        ovenCookTimeMultiplier = config.getFloat("Oven Cook Time Multiplier", "general", 1f, 0.25f, 9f, "The cooking time multiplier for the cooking oven. Higher values means it will take longer.");
        cowJarMilkPerTick = config.getFloat("Cow in a Jar Milk per Tick", "general", 0.5f, 0f, Float.MAX_VALUE, "The amount of milk the cow in a jar generates per tick.");

        ovenRequiresCookingOil = config.getBoolean("Oven Requires Cooking Oil", "compat", false, "Set this to true if you'd like the oven to only accept cooking oil as fuel (requires Pam's Harvestcraft)");

        cowJarEnabled = config.getBoolean("Cow in a Jar", "general", true, "If true, a cow can be squished into a Milk Jar by dropping an anvil on top.");

        config.setCategoryComment("blocks", "Setting any of these options to false will disable their crafting recipe.");
        config.setCategoryComment("items", "Setting any of these options to false will disable their crafting recipe.");
        config.setCategoryComment("modules", "Setting any of these options to false will disable their specific mod support.");
    }

}
