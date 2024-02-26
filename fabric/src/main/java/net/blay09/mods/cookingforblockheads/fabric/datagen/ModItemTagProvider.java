package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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

        final var connectors = getOrCreateTagBuilder(ModItemTags.CONNECTORS);
        connectors.add(ModBlocks.connector.asItem());
        for (final var connector : ModBlocks.connectors) {
            connectors.add(connector.asItem());
        }

        final var dyedConnectors = getOrCreateTagBuilder(ModItemTags.DYED_CONNECTORS);
        for (final var connector : ModBlocks.connectors) {
            dyedConnectors.add(connector.asItem());
        }
    }
}
