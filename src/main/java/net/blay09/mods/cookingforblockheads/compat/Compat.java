package net.blay09.mods.cookingforblockheads.compat;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;

public class Compat {

	public static final String PAMS_HARVESTCRAFT = "harvestcraft";
	public static final String MORE_FOOD = "morefood";
	public static final String EXTRA_FOOD = "extrafood";
	public static final String FOOD_EXPANSION = "fe"; // pls
	public static final String VANILLA_FOOD_PANTRY = "vanillafoodpantry";
	public static final String CRAFTTWERKER = "crafttweaker";
	public static final String IMMERSIVE_ENGINEERING = "immersiveengineering";
	public static final String THEONEPROBE = "theoneprobe";
	public static final String WAILA = "waila";
	public static final String APPLECORE = "applecore";
	public static final String MOUSE_TWEAKS = "mousetweaks";
	public static final String INVENTORY_TWEAKS = "inventorytweaks";
	public static final String QUARK = "quark";
	public static final String ACTUALLY_ADDITIONS = "actuallyadditions";

	private static Fluid milkFluid = null;

	@Nullable
	public static Fluid getMilkFluid() {
		if (milkFluid == null) {
			milkFluid = FluidRegistry.getFluid("milk");
		}
		return milkFluid;
	}
}
