package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.Balm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class Compat {

    public static final String HARVESTCRAFT_FOOD_CORE = "pamhc2foodcore";
    public static final String THEONEPROBE = "theoneprobe";
    public static final String APPLECORE = "applecore";
    public static final String EX_COMPRESSUM = "excompressum";
    public static final String SPICE_OF_LIFE = "solcarrot";

    public static Fluid getMilkFluid() {
        return Balm.modSupport().milkFluid().get();
    }

    private static TagKey<Item> cookingOilTag;
    @Deprecated(forRemoval = true)
    public static TagKey<Item> getCookingOilTag() {
        if(cookingOilTag == null) {
            cookingOilTag = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "cooking_oil"));
        }
        return cookingOilTag;
    }
}
