package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        final var ovens = builder(ModItemTags.OVENS);
        ModBlocks.ovens.sortedValues().map(DeferredBlock::asBlockItemId).forEach(ovens::add);

        final var dyedOvens = builder(ModItemTags.DYED_OVENS);
        ModBlocks.ovens.sortedValues()
                .filter(it -> it != ModBlocks.ovens.get(DyeColor.WHITE))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedOvens::add);

        final var fridges = builder(ModItemTags.FRIDGES);
        ModBlocks.fridges.sortedValues().map(DeferredBlock::asBlockItemId).forEach(fridges::add);

        final var dyedFridges = builder(ModItemTags.DYED_FRIDGES);
        ModBlocks.fridges.sortedValues()
                .filter(it -> it != ModBlocks.fridges.get(DyeColor.WHITE))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedFridges::add);

        final var sinks = builder(ModItemTags.SINKS).add(ModBlocks.sinks.get(null).asBlockItemId());
        ModBlocks.sinks.sortedValues().map(DeferredBlock::asBlockItemId).forEach(sinks::add);

        final var dyedSinks = builder(ModItemTags.DYED_SINKS);
        ModBlocks.sinks.sortedValues().map(DeferredBlock::asBlockItemId).forEach(dyedSinks::add);

        final var chickenSinks = builder(ModItemTags.CHICKEN_SINKS).add(ModBlocks.chickenSinks.get(null).asBlockItemId());
        ModBlocks.chickenSinks.sortedValues().map(DeferredBlock::asBlockItemId).forEach(chickenSinks::add);

        final var dyedChickenSinks = builder(ModItemTags.DYED_CHICKEN_SINKS);
        ModBlocks.chickenSinks.sortedValues()
                .filter(it -> it != ModBlocks.chickenSinks.get(null))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedChickenSinks::add);

        final var cookingTables = builder(ModItemTags.COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues().map(DeferredBlock::asBlockItemId).forEach(cookingTables::add);

        final var dyedCookingTables = builder(ModItemTags.DYED_COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues()
                .filter(it -> it != ModBlocks.cookingTables.get(null))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedCookingTables::add);

        final var counters = builder(ModItemTags.COUNTERS);
        ModBlocks.counters.sortedValues().map(DeferredBlock::asBlockItemId).forEach(counters::add);

        final var dyedCounters = builder(ModItemTags.DYED_COUNTERS);
        ModBlocks.counters.sortedValues()
                .filter(it -> it != ModBlocks.counters.get(null))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedCounters::add);

        final var cabinets = builder(ModItemTags.CABINETS);
        ModBlocks.cabinets.sortedValues().map(DeferredBlock::asBlockItemId).forEach(cabinets::add);

        final var dyedCabinets = builder(ModItemTags.DYED_CABINETS);
        ModBlocks.cabinets.sortedValues()
                .filter(it -> it != ModBlocks.cabinets.get(null))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedCabinets::add);

        final var connectors = builder(ModItemTags.CONNECTORS);
        ModBlocks.connectors.sortedValues().map(DeferredBlock::asBlockItemId).forEach(connectors::add);

        final var dyedConnectors = builder(ModItemTags.DYED_CONNECTORS);
        ModBlocks.connectors.sortedValues()
                .filter(it -> it != ModBlocks.connectors.get(null))
                .map(DeferredBlock::asBlockItemId)
                .forEach(dyedConnectors::add);
    }
}
