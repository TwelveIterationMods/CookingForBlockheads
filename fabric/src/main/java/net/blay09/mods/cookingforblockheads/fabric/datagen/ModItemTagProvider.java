package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
        for (final var oven : ModBlocks.ovens) {
            ovens.add(oven.asItem());
        }

        final var dyedOvens = tag(ModItemTags.DYED_OVENS);
        for (final var oven : ModBlocks.ovens) {
            if (oven.getColor() != DyeColor.WHITE) {
                dyedOvens.add(oven.asItem());
            }
        }

        final var fridges = tag(ModItemTags.FRIDGES);
        for (final var fridge : ModBlocks.fridges) {
            fridges.add(fridge.asItem());
        }

        final var dyedFridges = tag(ModItemTags.DYED_FRIDGES);
        for (final var fridge : ModBlocks.fridges) {
            if (fridge.getColor() != DyeColor.WHITE) {
                dyedFridges.add(fridge.asItem());
            }
        }

        final var sinks = tag(ModItemTags.SINKS).add(ModBlocks.sink.asItem());
        final var dyedSinks = tag(ModItemTags.DYED_SINKS);
        for (final var sink : ModBlocks.dyedSinks) {
            sinks.add(sink.asItem());
            dyedSinks.add(sink.asItem());
        }

        final var cookingTables = tag(ModItemTags.COOKING_TABLES).add(ModBlocks.cookingTable.asItem());
        final var dyedCookingTables = tag(ModItemTags.DYED_COOKING_TABLES);
        for (final var cookingTable : ModBlocks.dyedCookingTables) {
            cookingTables.add(cookingTable.asItem());
            dyedCookingTables.add(cookingTable.asItem());
        }

        final var counters = tag(ModItemTags.COUNTERS).add(ModBlocks.counter.asItem());
        final var dyedCounters = tag(ModItemTags.DYED_COUNTERS);
        for (final var counter : ModBlocks.dyedCounters) {
            counters.add(counter.asItem());
            dyedCounters.add(counter.asItem());
        }

        final var cabinets = tag(ModItemTags.CABINETS).add(ModBlocks.cabinet.asItem());
        final var dyedCabinets = tag(ModItemTags.DYED_CABINETS);
        for (final var cabinet : ModBlocks.dyedCabinets) {
            cabinets.add(cabinet.asItem());
            dyedCabinets.add(cabinet.asItem());
        }

        final var connectors = tag(ModItemTags.CONNECTORS);
        connectors.add(ModBlocks.connector.asItem());
        for (final var connector : ModBlocks.dyedConnectors) {
            connectors.add(connector.asItem());
        }

        final var dyedConnectors = tag(ModItemTags.DYED_CONNECTORS);
        for (final var connector : ModBlocks.dyedConnectors) {
            dyedConnectors.add(connector.asItem());
        }
    }
}
