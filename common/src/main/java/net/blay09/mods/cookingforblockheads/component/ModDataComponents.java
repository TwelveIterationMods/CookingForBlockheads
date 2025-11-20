package net.blay09.mods.cookingforblockheads.component;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Unit;

public class ModDataComponents {

    public static Holder<DataComponentType<Unit>> toasted;
    public static Holder<DataComponentType<MultiblockKitchenComponent>> multiblockKitchen;

    public static void initialize(BalmDataComponentTypeRegistrar components) {
        toasted = components.register("toasted", MapCodec.unitCodec(Unit.INSTANCE)).asHolder();
        multiblockKitchen = components.register("multiblock_kitchen", MultiblockKitchenComponent.CODEC).asHolder();
    }
}
