package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeMod;

public class Compat {

    public static final String PAMS_HARVESTCRAFT = "harvestcraft";
    public static final String CRAFTTWEAKER = "crafttweaker";
    public static final String IMMERSIVE_ENGINEERING = "immersiveengineering";
    public static final String THEONEPROBE = "theoneprobe";
    public static final String APPLECORE = "applecore";
    public static final String INVENTORY_TWEAKS = "inventorytweaks";
    public static final String QUARK = "quark";

    public static Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }

    public static Item cuttingBoardItem = Items.AIR;

}
