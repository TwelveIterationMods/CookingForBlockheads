package net.blay09.mods.cookingforblockheads.fabric.datagen;

import com.google.common.collect.Comparators;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture, (item) -> item.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        final var ovens = tag(ModItemTags.OVENS);
        ModBlocks.ovens.sortedValues().map(ItemLike::asItem).forEach(ovens::add);

        final var dyedOvens = tag(ModItemTags.DYED_OVENS);
        ModBlocks.ovens.sortedValues()
                .filter(it -> it != ModBlocks.ovens.get(DyeColor.WHITE))
                .map(ItemLike::asItem)
                .forEach(dyedOvens::add);

        final var fridges = tag(ModItemTags.FRIDGES);
        ModBlocks.fridges.sortedValues().map(ItemLike::asItem).forEach(fridges::add);

        final var dyedFridges = tag(ModItemTags.DYED_FRIDGES);
        ModBlocks.fridges.sortedValues()
                .filter(it -> it != ModBlocks.fridges.get(DyeColor.WHITE))
                .map(ItemLike::asItem)
                .forEach(dyedFridges::add);

        final var sinks = tag(ModItemTags.SINKS).add(ModBlocks.sinks.get(null).asItem());
        ModBlocks.sinks.sortedValues().map(ItemLike::asItem).forEach(sinks::add);

        final var dyedSinks = tag(ModItemTags.DYED_SINKS);
        ModBlocks.sinks.sortedValues().map(ItemLike::asItem).forEach(dyedSinks::add);

        final var cookingTables = tag(ModItemTags.COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues().map(ItemLike::asItem).forEach(cookingTables::add);

        final var dyedCookingTables = tag(ModItemTags.DYED_COOKING_TABLES);
        ModBlocks.cookingTables.sortedValues()
                .filter(it -> it != ModBlocks.cookingTables.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCookingTables::add);

        final var counters = tag(ModItemTags.COUNTERS);
        ModBlocks.counters.sortedValues().map(ItemLike::asItem).forEach(counters::add);

        final var dyedCounters = tag(ModItemTags.DYED_COUNTERS);
        ModBlocks.counters.sortedValues()
                .filter(it -> it != ModBlocks.counters.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCounters::add);

        final var cabinets = tag(ModItemTags.CABINETS);
        ModBlocks.cabinets.sortedValues().map(ItemLike::asItem).forEach(cabinets::add);

        final var dyedCabinets = tag(ModItemTags.DYED_CABINETS);
        ModBlocks.cabinets.sortedValues()
                .filter(it -> it != ModBlocks.cabinets.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedCabinets::add);

        final var connectors = tag(ModItemTags.CONNECTORS);
        ModBlocks.connectors.sortedValues().map(ItemLike::asItem).forEach(connectors::add);

        final var dyedConnectors = tag(ModItemTags.DYED_CONNECTORS);
        ModBlocks.connectors.sortedValues()
                .filter(it -> it != ModBlocks.connectors.get(null))
                .map(ItemLike::asItem)
                .forEach(dyedConnectors::add);
    }
}
