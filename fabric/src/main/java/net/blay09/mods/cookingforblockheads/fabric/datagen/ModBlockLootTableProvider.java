package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(dataOutput, provider);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.cookingTable);
        for (final var cookingTable : ModBlocks.dyedCookingTables) {
            dropSelf(cookingTable);
        }
        dropSelf(ModBlocks.toaster);
        dropSelf(ModBlocks.milkJar);
        dropSelf(ModBlocks.cowJar);
        dropSelf(ModBlocks.cuttingBoard);
        dropSelf(ModBlocks.sink);
        for (final var sink : ModBlocks.dyedSinks) {
            dropSelf(sink);
        }
        dropSelf(ModBlocks.connector);
        for (final var oven : ModBlocks.ovens) {
            add(oven, this::createNameableBlockEntityTable);
        }
        for (final var fridge : ModBlocks.fridges) {
            add(fridge, this::createNameableBlockEntityTable);
        }
        add(ModBlocks.toolRack, this::createNameableBlockEntityTable);
        add(ModBlocks.spiceRack, this::createNameableBlockEntityTable);
        add(ModBlocks.fruitBasket, this::createNameableBlockEntityTable);
        add(ModBlocks.counter, this::createNameableBlockEntityTable);
        for (final var counter : ModBlocks.dyedCounters) {
            add(counter, this::createNameableBlockEntityTable);
        }
        add(ModBlocks.cabinet, this::createNameableBlockEntityTable);
        for (final var cabinet : ModBlocks.dyedCabinets) {
            add(cabinet, this::createNameableBlockEntityTable);
        }
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            dropSelf(kitchenFloor);
        }
        for (final var connector : ModBlocks.dyedConnectors) {
            dropSelf(connector);
        }
    }
}
