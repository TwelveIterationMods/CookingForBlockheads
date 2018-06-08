package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;

public class Compat {

	public static final String PAMS_HARVESTCRAFT = "harvestcraft";
	public static final String CRAFTTWEAKER = "crafttweaker";
	public static final String IMMERSIVE_ENGINEERING = "immersiveengineering";
	public static final String THEONEPROBE = "theoneprobe";
	public static final String APPLECORE = "applecore";
	public static final String INVENTORY_TWEAKS = "inventorytweaks";
	public static final String QUARK = "quark";

	private static Fluid milkFluid = null;

	@Nullable
	public static Fluid getMilkFluid() {
		if (milkFluid == null) {
			milkFluid = FluidRegistry.getFluid("milk");
		}
		return milkFluid;
	}

	public static Item cuttingBoardItem = Items.AIR;

}
