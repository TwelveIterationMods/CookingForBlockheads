package net.blay09.mods.cookingforblockheads.compat;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class Compat {

    public static final String PAMS_HARVESTCRAFT = "harvestcraft";
    public static final String CRAFTTWEAKER = "crafttweaker";
    public static final String IMMERSIVE_ENGINEERING = "immersiveengineering";
    public static final String THEONEPROBE = "theoneprobe";
    public static final String APPLECORE = "applecore";
    public static final String INVENTORY_TWEAKS = "inventorytweaks";
    public static final String QUARK = "quark";

    private static boolean milkFluidInitialized = false;
    private static Fluid milkFluid = null;

    @Nullable
    public static Fluid getMilkFluid() {
        if (!milkFluidInitialized) {
            milkFluidInitialized = true;
            ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
            if(FluidUtil.getFluidHandler(milkBucket).isPresent()) {
                milkFluid = FluidUtil.getFluidContained(milkBucket).get().getFluid();
            }
        }

        return milkFluid;
    }

    public static Item cuttingBoardItem = Items.AIR;

}
