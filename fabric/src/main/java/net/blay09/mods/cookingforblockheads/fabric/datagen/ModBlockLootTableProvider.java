package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
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
        ModBlocks.ovens.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.fridges.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        add(ModBlocks.toolRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.spiceRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.fruitBasket.value(), this::createNameableBlockEntityTable);
        ModBlocks.counters.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.cabinets.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.kitchenFloors.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.connectors.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.sinks.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.cookingTables.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
    }
}
