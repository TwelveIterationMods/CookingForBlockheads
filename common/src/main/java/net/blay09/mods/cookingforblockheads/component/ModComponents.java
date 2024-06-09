package net.blay09.mods.cookingforblockheads.component;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

public class ModComponents {

    public static DeferredObject<DataComponentType<Unit>> toasted;

    public static void initialize(BalmComponents components) {
        toasted = components.registerComponent(() -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).build(),
                ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "toasted"));
    }
}
