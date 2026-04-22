package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.Balm;
import net.minecraft.world.level.material.Fluid;

public class Compat {

    public static final String HARVESTCRAFT_FOOD_CORE = "pamhc2foodcore";
    public static final String APPLECORE = "applecore";
    public static final String EX_COMPRESSUM = "excompressum";
    public static final String SPICE_OF_LIFE = "solcarrot";
    public static final String FARMERS_DELIGHT = "farmersdelight";

    public static Fluid getMilkFluid() {
        return Balm.modSupport().milkFluid().get();
    }

}
