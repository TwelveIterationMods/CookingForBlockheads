package net.blay09.mods.cookingforblockheads.component;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Unit;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModComponents {

    public static DeferredObject<DataComponentType<Unit>> toasted;
    public static DeferredObject<DataComponentType<MultiblockKitchenComponent>> multiblockKitchen;

    public static void initialize(BalmComponents components) {
        toasted = components.registerComponent(() -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).build(),
                id("toasted"));
        multiblockKitchen = components.registerComponent(() -> DataComponentType.<MultiblockKitchenComponent>builder().persistent(MultiblockKitchenComponent.CODEC).build(),
                id("multiblock_kitchen"));
    }
}
