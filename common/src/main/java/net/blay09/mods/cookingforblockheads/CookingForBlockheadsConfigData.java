package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;

@Config(CookingForBlockheads.MOD_ID)
public class CookingForBlockheadsConfigData implements BalmConfigData {

    @Comment("If true, a cow can be squished into a Milk Jar by dropping an anvil on top.")
    public boolean cowJarEnabled = true;

    @Comment("The amount of milk the cow in a jar generates per tick.")
    public int cowJarMilkPerTick = 1; // 1 - max

    @Comment("Ex Compressum compatibility. Multiplier applied to the milk per tick for Compressed Cow in a Jar.")
    public double compressedCowJarMilkMultiplier = 9; // 0 - max

    @Comment("Set this to true if you'd like the sink to require water to be piped in, instead of providing infinite of it.")
    public boolean sinkRequiresWater = false;

    @Comment("Enabling this will make the kitchen counters have twice as much inventory space.")
    public boolean largeCounters = false;

    @Comment("Set this to true if you'd like to disallow automation of the oven (pipes and such won't be able to insert/extract)")
    public boolean disallowOvenAutomation = false;

    @Comment("The fuel multiplier for the cooking oven. Higher values means fuel lasts longer, 1.0 is furnace default.")
    public double ovenFuelTimeMultiplier = 0.33f; // 0.1 - 2

    @Comment("The cooking time multiplier for the cooking oven. Higher values means it will take longer.")
    public double ovenCookTimeMultiplier = 1f; // 0.25 - 9

    @Comment("Set this to true if you'd like the oven to only accept cooking oil as fuel (requires Pam's Harvestcraft)")
    public boolean ovenRequiresCookingOil = false;

    @Comment("Set to false if you don't want ingredients to be marked with a special icon in the recipe book.")
    public boolean showIngredientIcon = true;

    @Comment("Toasting toasted bread again will turn into charcoal (only if no other mod adding toast is present). Set to false to disable.")
    public boolean allowVeryToastedBread = true;
}
