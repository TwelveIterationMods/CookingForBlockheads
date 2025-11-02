package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture, (item) -> item.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        final var ovens = tag(ModItemTags.OVENS);
        ModBlocks.ovens.forEach(it -> ovens.add(it.asItem()));

        final var dyedOvens = tag(ModItemTags.DYED_OVENS);
        ModBlocks.ovens.forEach((color, block) -> {
            if (color != DyeColor.WHITE) {
                dyedOvens.add(block.asItem());
            }
        });

        final var fridges = tag(ModItemTags.FRIDGES);
        ModBlocks.fridges.forEach(it -> fridges.add(it.asItem()));

        final var dyedFridges = tag(ModItemTags.DYED_FRIDGES);
        ModBlocks.fridges.forEach((color, block) -> {
            if (color != DyeColor.WHITE) {
                dyedFridges.add(block.asItem());
            }
        });

        final var sinks = tag(ModItemTags.SINKS).add(ModBlocks.sinks.getUndiscriminated().asItem());
        ModBlocks.sinks.forEach(it -> sinks.add(it.asItem()));

        final var dyedSinks = tag(ModItemTags.DYED_SINKS);
        ModBlocks.sinks.forEachDiscriminated((color, block) -> dyedSinks.add(block.asItem()));

        final var cookingTables = tag(ModItemTags.COOKING_TABLES);
        ModBlocks.cookingTables.forEach(it -> cookingTables.add(it.asItem()));

        final var dyedCookingTables = tag(ModItemTags.DYED_COOKING_TABLES);
        ModBlocks.cookingTables.forEachDiscriminated((color, block) -> dyedCookingTables.add(block.asItem()));

        final var counters = tag(ModItemTags.COUNTERS);
        ModBlocks.counters.forEach(it -> counters.add(it.asItem()));

        final var dyedCounters = tag(ModItemTags.DYED_COUNTERS);
        ModBlocks.counters.forEachDiscriminated((color, block) -> dyedCounters.add(block.asItem()));

        final var cabinets = tag(ModItemTags.CABINETS);
        ModBlocks.cabinets.forEach(it -> cabinets.add(it.asItem()));

        final var dyedCabinets = tag(ModItemTags.DYED_CABINETS);
        ModBlocks.cabinets.forEachDiscriminated((color, block) -> dyedCabinets.add(block.asItem()));

        final var connectors = tag(ModItemTags.CONNECTORS);
        ModBlocks.connectors.forEach(it -> connectors.add(it.asItem()));

        final var dyedConnectors = tag(ModItemTags.DYED_CONNECTORS);
        ModBlocks.connectors.forEachDiscriminated((color, block) -> dyedConnectors.add(block.asItem()));
    }
}
