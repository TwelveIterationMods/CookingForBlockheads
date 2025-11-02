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
        ModBlocks.ovens.forEach((discriminator, it) -> ovens.add(it.asItem()));

        final var dyedOvens = tag(ModItemTags.DYED_OVENS);
        ModBlocks.ovens.forEach((color, block) -> {
            if (color != DyeColor.WHITE) {
                dyedOvens.add(block.asItem());
            }
        });

        final var fridges = tag(ModItemTags.FRIDGES);
        ModBlocks.fridges.forEach((discriminator, it) -> fridges.add(it.asItem()));

        final var dyedFridges = tag(ModItemTags.DYED_FRIDGES);
        ModBlocks.fridges.forEach((color, block) -> {
            if (color != DyeColor.WHITE) {
                dyedFridges.add(block.asItem());
            }
        });

        final var sinks = tag(ModItemTags.SINKS).add(ModBlocks.sinks.get(null).asItem());
        ModBlocks.sinks.forEach((discriminator, it) -> sinks.add(it.asItem()));

        final var dyedSinks = tag(ModItemTags.DYED_SINKS);
        ModBlocks.sinks.forEach((color, block) -> dyedSinks.add(block.asItem()));

        final var cookingTables = tag(ModItemTags.COOKING_TABLES);
        ModBlocks.cookingTables.forEach((discriminator, it) -> cookingTables.add(it.asItem()));

        final var dyedCookingTables = tag(ModItemTags.DYED_COOKING_TABLES);
        ModBlocks.cookingTables.filterNonNullDiscriminators().forEach(block -> dyedCookingTables.add(block.asItem()));

        final var counters = tag(ModItemTags.COUNTERS);
        ModBlocks.counters.forEach((discriminator, it) -> counters.add(it.asItem()));

        final var dyedCounters = tag(ModItemTags.DYED_COUNTERS);
        ModBlocks.counters.filterNonNullDiscriminators().forEach(block -> dyedCounters.add(block.asItem()));

        final var cabinets = tag(ModItemTags.CABINETS);
        ModBlocks.cabinets.forEach((discriminator, it) -> cabinets.add(it.asItem()));

        final var dyedCabinets = tag(ModItemTags.DYED_CABINETS);
        ModBlocks.cabinets.filterNonNullDiscriminators().forEach(block -> dyedCabinets.add(block.asItem()));

        final var connectors = tag(ModItemTags.CONNECTORS);
        ModBlocks.connectors.forEach((discriminator, it) -> connectors.add(it.asItem()));

        final var dyedConnectors = tag(ModItemTags.DYED_CONNECTORS);
        ModBlocks.connectors.filterNonNullDiscriminators().forEach(block -> dyedConnectors.add(block.asItem()));
    }
}
