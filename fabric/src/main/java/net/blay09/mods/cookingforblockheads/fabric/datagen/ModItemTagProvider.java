package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        final var ovens = valueLookupBuilder(ModItemTags.OVENS);
        ModBlocks.ovens.sortedValues().map(ItemLike::asItem).forEach(ovens::add);

        final var dyedOvens = valueLookupBuilder(ModItemTags.DYED_OVENS);
        ModBlocks.ovens.sortedValues()
                .filter(it -> it != ModBlocks.ovens.get(DyeColor.WHITE))
                .map(ItemLike::asItem)
                .forEach(dyedOvens::add);

        final var fridges = valueLookupBuilder(ModItemTags.FRIDGES);
        ModBlocks.fridges.sortedValues().map(ItemLike::asItem).forEach(fridges::add);

        final var dyedFridges = valueLookupBuilder(ModItemTags.DYED_FRIDGES);
        ModBlocks.fridges.sortedValues()
                .filter(it -> it != ModBlocks.fridges.get(DyeColor.WHITE))
                .map(ItemLike::asItem)
                .forEach(dyedFridges::add);

        final var sinks = valueLookupBuilder(ModItemTags.SINKS).add(ModBlocks.sinks.get(null).asItem());
        ModBlocks.sinks.sortedValues().map(ItemLike::asItem).forEach(sinks::add);

        final var dyedSinks = valueLookupBuilder(ModItemTags.DYED_SINKS);
        ModBlocks.sinks.sortedValues().map(ItemLike::asItem).forEach(dyedSinks::add);

        final var cookingTables = valueLookupBuilder(ModItemTags.COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues().map(ItemLike::asItem).forEach(cookingTables::add);

        final var dyedCookingTables = valueLookupBuilder(ModItemTags.DYED_COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues()
                .filter(it -> it != ModBlocks.cookingTables.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCookingTables::add);

        final var counters = valueLookupBuilder(ModItemTags.COUNTERS);
        ModBlocks.counters.sortedValues().map(ItemLike::asItem).forEach(counters::add);

        final var dyedCounters = valueLookupBuilder(ModItemTags.DYED_COUNTERS);
        ModBlocks.counters.sortedValues()
                .filter(it -> it != ModBlocks.counters.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCounters::add);

        final var cabinets = valueLookupBuilder(ModItemTags.CABINETS);
        ModBlocks.cabinets.sortedValues().map(ItemLike::asItem).forEach(cabinets::add);

        final var dyedCabinets = valueLookupBuilder(ModItemTags.DYED_CABINETS);
        ModBlocks.cabinets.sortedValues()
                .filter(it -> it != ModBlocks.cabinets.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCabinets::add);

        final var connectors = valueLookupBuilder(ModItemTags.CONNECTORS);
        ModBlocks.connectors.sortedValues().map(ItemLike::asItem).forEach(connectors::add);

        final var dyedConnectors = valueLookupBuilder(ModItemTags.DYED_CONNECTORS);
        ModBlocks.connectors.sortedValues()
                .filter(it -> it != ModBlocks.connectors.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedConnectors::add);
    }
}
