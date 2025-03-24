package net.blay09.mods.cookingforblockheads.capability;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModCapabilities {

    public static CapabilityType<Block, KitchenItemProvider, Void> KITCHEN_ITEM_PROVIDER;
    public static CapabilityType<Block, KitchenItemProcessor, Void> KITCHEN_ITEM_PROCESSOR;

    public static void initialize(BalmCapabilities capabilities) {
        KITCHEN_ITEM_PROVIDER = capabilities.registerType(id("container"), Block.class, KitchenItemProvider.class, Void.class);
        KITCHEN_ITEM_PROCESSOR = capabilities.registerType(id("fluid_tank"), Block.class, KitchenItemProcessor.class, Void.class);

        capabilities.registerProvider(id("kitchen_item_provider"),
                KITCHEN_ITEM_PROVIDER,
                (blockEntity, context) -> blockEntity instanceof KitchenItemProviderHolder holder ? holder.getKitchenItemProvider() : null,
                () -> List.of(
                        ModBlockEntities.counter.get(),
                        ModBlockEntities.cabinet.get(),
                        ModBlockEntities.fridge.get(),
                        ModBlockEntities.milkJar.get(),
                        ModBlockEntities.cowJar.get(),
                        ModBlockEntities.spiceRack.get(),
                        ModBlockEntities.sink.get(),
                        ModBlockEntities.oven.get(),
                        ModBlockEntities.fruitBasket.get()
                ));

        capabilities.registerProvider(id("kitchen_item_processor"),
                KITCHEN_ITEM_PROCESSOR,
                (blockEntity, context) -> blockEntity instanceof KitchenItemProcessorHolder holder ? holder.getKitchenItemProcessor() : null,
                () -> List.of(ModBlockEntities.oven.get()));
    }
}
