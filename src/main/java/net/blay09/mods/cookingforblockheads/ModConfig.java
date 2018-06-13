package net.blay09.mods.cookingforblockheads;

import net.minecraftforge.common.config.Config;

@Config(modid = CookingForBlockheads.MOD_ID, category = "")
public class ModConfig {

    public static General general = new General();

    public static Client client = new Client();

    public static Compat compat = new Compat();

    public static class General {
        @Config.Name("Cow in a Jar")
        @Config.Comment("If true, a cow can be squished into a Milk Jar by dropping an anvil on top.")
        public boolean cowJarEnabled = true;

        @Config.Name("Cow in a Jar Milk per Tick")
        @Config.Comment("The amount of milk the cow in a jar generates per tick.")
        @Config.RangeDouble(min = 0)
        public float cowJarMilkPerTick = 0.5f;

        @Config.Name("Sink Requires Water")
        @Config.Comment("Set this to true if you'd like the sink to require water to be piped in, instead of providing infinite of it.")
        public boolean sinkRequiresWater = false;

        @Config.Name("Large Counters")
        @Config.Comment("Enabling this will make the kitchen counters have twice as much inventory space.")
        public boolean largeCounters = false;

        @Config.Name("Disallow Oven Automation")
        @Config.Comment("Set this to true if you'd like to disallow automation of the oven (pipes and such won't be able to insert/extract)")
        public boolean disallowOvenAutomation = false;

        @Config.Name("Oven Fuel Time Multiplier")
        @Config.Comment("The fuel multiplier for the cooking oven. Higher values means fuel lasts longer, 1.0 is furnace default.")
        @Config.RangeDouble(min = 0.1f, max = 0.2f)
        public float ovenFuelTimeMultiplier = 0.33f;

        @Config.Name("Oven Cook Time Multiplier")
        @Config.Comment("The cooking time multiplier for the cooking oven. Higher values means it will take longer.")
        @Config.RangeDouble(min = 0.25f, max = 9f)
        public float ovenCookTimeMultiplier = 1f;
    }

    public static class Client {
        @Config.Name("Show Ingredient Icon")
        @Config.Comment("Set to false if you don't want ingredients to be marked with a special icon in the recipe book.")
        public boolean showIngredientIcon = true;
    }

    public static class Compat {
        @Config.Name("Oven Requires Cooking Oil")
        @Config.Comment("Set this to true if you'd like the oven to only accept cooking oil as fuel (requires Pam's Harvestcraft)")
        public boolean ovenRequiresCookingOil = false;
    }

}
