package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeMod;

public class Compat {

    public static final String HARVESTCRAFT_FOOD_CORE = "pamhc2foodcore";
    public static final String THEONEPROBE = "theoneprobe";
    public static final String APPLECORE = "applecore";
    public static final String EX_COMPRESSUM = "excompressum";

    public static Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }

    public static Item cuttingBoardItem = Items.AIR;

}
