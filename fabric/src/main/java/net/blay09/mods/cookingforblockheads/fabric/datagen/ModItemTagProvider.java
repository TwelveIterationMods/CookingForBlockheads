package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(ModItemTags.IS_DYEABLE).add(ModBlocks.cookingTable.asItem(),
                ModBlocks.fridge.asItem(),
                ModBlocks.sink.asItem(),
                ModBlocks.counter.asItem(),
                ModBlocks.cabinet.asItem());

        final var ovens = getOrCreateTagBuilder(ModItemTags.OVENS);
        for (final var oven : ModBlocks.ovens) {
            ovens.add(oven.asItem());
        }

        final var dyedOvens = getOrCreateTagBuilder(ModItemTags.DYED_OVENS);
        for (final var oven : ModBlocks.ovens) {
            if (oven.getColor() != DyeColor.WHITE) {
                dyedOvens.add(oven.asItem());
            }
        }

        final var sinks = getOrCreateTagBuilder(ModItemTags.SINKS).add(ModBlocks.sink.asItem());
        final var dyedSinks = getOrCreateTagBuilder(ModItemTags.DYED_SINKS);
        for (final var sink : ModBlocks.dyedSinks) {
            sinks.add(sink.asItem());
            dyedSinks.add(sink.asItem());
        }

        final var cookingTables = getOrCreateTagBuilder(ModItemTags.COOKING_TABLES).add(ModBlocks.cookingTable.asItem());
        final var dyedCookingTables = getOrCreateTagBuilder(ModItemTags.DYED_COOKING_TABLES);
        for (final var cookingTable : ModBlocks.dyedCookingTables) {
            cookingTables.add(cookingTable.asItem());
            dyedCookingTables.add(cookingTable.asItem());
        }

        final var counters = getOrCreateTagBuilder(ModItemTags.COUNTERS).add(ModBlocks.counter.asItem());
        final var dyedCounters = getOrCreateTagBuilder(ModItemTags.DYED_COUNTERS);
        for (final var counter : ModBlocks.dyedCounters) {
            counters.add(counter.asItem());
            dyedCounters.add(counter.asItem());
        }

        final var cabinets = getOrCreateTagBuilder(ModItemTags.CABINETS).add(ModBlocks.cabinet.asItem());
        final var dyedCabinets = getOrCreateTagBuilder(ModItemTags.DYED_CABINETS);
        for (final var cabinet : ModBlocks.dyedCabinets) {
            cabinets.add(cabinet.asItem());
            dyedCabinets.add(cabinet.asItem());
        }

        final var connectors = getOrCreateTagBuilder(ModItemTags.CONNECTORS);
        connectors.add(ModBlocks.connector.asItem());
        for (final var connector : ModBlocks.dyedConnectors) {
            connectors.add(connector.asItem());
        }

        final var dyedConnectors = getOrCreateTagBuilder(ModItemTags.DYED_CONNECTORS);
        for (final var connector : ModBlocks.dyedConnectors) {
            dyedConnectors.add(connector.asItem());
        }
    }
}
