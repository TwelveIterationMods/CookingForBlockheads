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
        dropSelf(ModBlocks.toaster.value());
        dropSelf(ModBlocks.milkJar.value());
        dropSelf(ModBlocks.cowJar.value());
        dropSelf(ModBlocks.cuttingBoard.value());
        ModBlocks.ovens.getAll().forEach(it -> add(it, this::createNameableBlockEntityTable));
        ModBlocks.fridges.getAll().forEach(it -> add(it, this::createNameableBlockEntityTable));
        add(ModBlocks.toolRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.spiceRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.fruitBasket.value(), this::createNameableBlockEntityTable);
        ModBlocks.counters.getAll().forEach(it -> add(it, this::createNameableBlockEntityTable));
        ModBlocks.cabinets.getAll().forEach(it -> add(it, this::createNameableBlockEntityTable));
        ModBlocks.kitchenFloors.getAll().forEach(this::dropSelf);
        ModBlocks.connectors.getAll().forEach(this::dropSelf);
        ModBlocks.sinks.getAll().forEach(this::dropSelf);
        ModBlocks.cookingTables.getAll().forEach(this::dropSelf);
    }
}
