package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class Compat {

    public static final String HARVESTCRAFT_FOOD_CORE = "pamhc2foodcore";
    public static final String THEONEPROBE = "theoneprobe";

    public static Fluid getMilkFluid() {
        return Balm.getRegistries().getMilkFluid();
    }

    private static TagKey<Item> cookingOilTag;
    @Deprecated(forRemoval = true)
    public static TagKey<Item> getCookingOilTag() {
        if(cookingOilTag == null) {
            cookingOilTag = Balm.getRegistries().getItemTag(ResourceLocation.fromNamespaceAndPath("c", "cooking_oil"));
        }
        return cookingOilTag;
    }
}
