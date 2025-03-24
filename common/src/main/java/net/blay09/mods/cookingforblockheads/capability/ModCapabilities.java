package net.blay09.mods.cookingforblockheads.capability;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModCapabilities {

    public static CapabilityType<Block, KitchenItemProvider, Void> KITCHEN_ITEM_PROVIDER;
    public static CapabilityType<Block, KitchenItemProcessor, Void> KITCHEN_ITEM_PROCESSOR;

    public static void initialize(BalmCapabilities capabilities) {
        KITCHEN_ITEM_PROVIDER = capabilities.registerType(id("container"), Block.class, KitchenItemProvider.class, Void.class);
        KITCHEN_ITEM_PROCESSOR = capabilities.registerType(id("fluid_tank"), Block.class, KitchenItemProcessor.class, Void.class);
    }
}
