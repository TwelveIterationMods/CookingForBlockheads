package net.blay09.mods.cookingforblockheads.capability;

import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.CapabilityType;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.world.level.block.Block;

import java.util.Set;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModCapabilities {

    public static CapabilityType<Block, KitchenItemProvider, Void> KITCHEN_ITEM_PROVIDER;
    public static CapabilityType<Block, KitchenItemProcessor, Void> KITCHEN_ITEM_PROCESSOR;

    public static void initialize(BalmCapabilities capabilities) {
        KITCHEN_ITEM_PROVIDER = capabilities.registerType(id("kitchen_item_provider"), Block.class, KitchenItemProvider.class, Void.class);
        KITCHEN_ITEM_PROCESSOR = capabilities.registerType(id("kitchen_item_processor"), Block.class, KitchenItemProcessor.class, Void.class);

        capabilities.registerProvider(id("kitchen_item_provider"),
                KITCHEN_ITEM_PROVIDER,
                (blockEntity, context) -> blockEntity instanceof KitchenItemProviderHolder holder ? holder.getKitchenItemProvider() : null,
                () -> Set.of(
                        ModBlockEntities.counter.value(),
                        ModBlockEntities.cabinet.value(),
                        ModBlockEntities.fridge.value(),
                        ModBlockEntities.milkJar.value(),
                        ModBlockEntities.cowJar.value(),
                        ModBlockEntities.spiceRack.value(),
                        ModBlockEntities.sink.value(),
                        ModBlockEntities.oven.value(),
                        ModBlockEntities.fruitBasket.value()
                ));

        capabilities.registerProvider(id("kitchen_item_processor"),
                KITCHEN_ITEM_PROCESSOR,
                (blockEntity, context) -> blockEntity instanceof KitchenItemProcessorHolder holder ? holder.getKitchenItemProcessor() : null,
                () -> Set.of(ModBlockEntities.oven.value()));
    }
}
