package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {
    protected ModBlockLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(dataOutput, provider);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.toaster.value());
        dropSelf(ModBlocks.milkJar.value());
        dropSelf(ModBlocks.cowJar.value());
        add(ModBlocks.cookieJar.value(), this::createNameableBlockEntityTable);
        dropSelf(ModBlocks.cuttingBoard.value());
        ModBlocks.ovens.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.fridges.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        add(ModBlocks.toolRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.spiceRack.value(), this::createNameableBlockEntityTable);
        add(ModBlocks.fruitBasket.value(), this::createNameableBlockEntityTable);
        ModBlocks.chickenSinks.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.counters.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.cabinets.forEach((discriminator, it) -> add(it.asBlock(), this::createNameableBlockEntityTable));
        ModBlocks.kitchenFloors.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.connectors.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.sinks.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
        ModBlocks.cookingTables.values().stream().map(DeferredBlock::asBlock).forEach(this::dropSelf);
    }
}
