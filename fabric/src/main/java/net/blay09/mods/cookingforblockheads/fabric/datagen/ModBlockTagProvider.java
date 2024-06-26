package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.CabinetBlock;
import net.blay09.mods.cookingforblockheads.block.CookingTableBlock;
import net.blay09.mods.cookingforblockheads.block.CounterBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxeTag = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("mineable/pickaxe"));
        final var mineablePickaxeBuilder = getOrCreateTagBuilder(mineablePickaxeTag);
        mineablePickaxeBuilder.add(ModBlocks.cookingTable,
                ModBlocks.sink,
                ModBlocks.counter,
                ModBlocks.cabinet,
                ModBlocks.toaster,
                ModBlocks.milkJar,
                ModBlocks.cowJar,
                ModBlocks.connector);
        for (final var fridge : ModBlocks.fridges) {
            mineablePickaxeBuilder.add(fridge);
        }
        for (final var oven : ModBlocks.ovens) {
            mineablePickaxeBuilder.add(oven);
        }
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            mineablePickaxeBuilder.add(kitchenFloor);
        }
        for (final var connector : ModBlocks.dyedConnectors) {
            mineablePickaxeBuilder.add(connector);
        }
        for (final var cookingTable : ModBlocks.dyedCookingTables) {
            mineablePickaxeBuilder.add(cookingTable);
        }
        for (final var cabinet : ModBlocks.dyedCabinets) {
            mineablePickaxeBuilder.add(cabinet);
        }
        for (final var counter : ModBlocks.dyedCounters) {
            mineablePickaxeBuilder.add(counter);
        }
        for (final var sink : ModBlocks.dyedSinks) {
            mineablePickaxeBuilder.add(sink);
        }

        final var mineableAxeTag = TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("mineable/axe"));
        final var mineableAxeBuilder = getOrCreateTagBuilder(mineableAxeTag);
        mineableAxeBuilder.add(ModBlocks.toolRack, ModBlocks.spiceRack, ModBlocks.fruitBasket, ModBlocks.cuttingBoard);

        final var kitchenItemProviders = getOrCreateTagBuilder(ModBlockTags.KITCHEN_ITEM_PROVIDERS);
        kitchenItemProviders.add(ModBlocks.sink, ModBlocks.counter, ModBlocks.cabinet, ModBlocks.fruitBasket, ModBlocks.spiceRack, ModBlocks.toolRack);
        for (final var cabinet : ModBlocks.dyedCabinets) {
            kitchenItemProviders.add(cabinet);
        }
        for (final var counter : ModBlocks.dyedCounters) {
            kitchenItemProviders.add(counter);
        }
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "basket"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "pantry"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "oak_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "spruce_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "birch_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "jungle_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "acacia_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "dark_oak_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "crimson_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "warped_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "mangrove_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "framed_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "oak_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "spruce_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "birch_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "jungle_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "acacia_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "dark_oak_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "crimson_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "warped_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "mangrove_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "framed_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "oak_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "spruce_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "birch_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "jungle_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "acacia_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "dark_oak_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "crimson_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "warped_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "mangrove_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "framed_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_maple_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_willow_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_cherry_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_wisteria_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_rosewood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_morado_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_yucca_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_kousa_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_aspen_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_grimwood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_driftwood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_river_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_poise_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_cypress_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_jacaranda_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_redbud_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_stripped_maple_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_willow_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_cherry_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_wisteria_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_rosewood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_morado_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_yucca_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_kousa_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_aspen_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_grimwood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_driftwood_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_river_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_stripped_poise_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_stripped_cypress_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_jacaranda_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_redbud_cabinet"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_maple_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_willow_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_cherry_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_wisteria_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_rosewood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_morado_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_yucca_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_kousa_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_aspen_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_grimwood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_driftwood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_river_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_poise_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_cypress_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_jacaranda_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_redbud_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_stripped_maple_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_willow_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_cherry_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_wisteria_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_rosewood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_morado_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_yucca_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_kousa_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_aspen_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_grimwood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_driftwood_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_river_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_stripped_poise_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_stripped_cypress_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_jacaranda_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_redbud_kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "kitchen_drawer"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "barrel"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "iron_barrel"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "gold_barrel"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "diamond_barrel"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "netherite_barrel"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_barrel_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_iron_barrel_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_gold_barrel_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_diamond_barrel_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_netherite_barrel_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_barrel_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_iron_barrel_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_gold_barrel_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_diamond_barrel_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_netherite_barrel_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_barrel_3"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_iron_barrel_3"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_gold_barrel_3"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_diamond_barrel_3"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_netherite_barrel_3"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_barrel_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_iron_barrel_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_gold_barrel_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_diamond_barrel_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "limited_netherite_barrel_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "chest"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "iron_chest"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "gold_chest"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "diamond_chest"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "netherite_chest"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "shulker_box"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "iron_shulker_box"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "gold_shulker_box"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "diamond_shulker_box"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "netherite_shulker_box"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "standard_drawers_1"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "standard_drawers_2"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "standard_drawers_4"));
        kitchenItemProviders.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "fractional_drawers_3"));

        final var cookingTables = getOrCreateTagBuilder(ModBlockTags.COOKING_TABLES);
        cookingTables.add(ModBlocks.cookingTable);
        for (final var cookingTable : ModBlocks.dyedCookingTables) {
            cookingTables.add(cookingTable);
        }

        final var kitchenConnectors = getOrCreateTagBuilder(ModBlockTags.KITCHEN_CONNECTORS);
        kitchenConnectors.add(ModBlocks.connector);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            kitchenConnectors.add(kitchenFloor);
        }
        for (final var connector : ModBlocks.dyedConnectors) {
            kitchenConnectors.add(connector);
        }
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_oak_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_oak_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_oak_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_oak_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_spruce_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_spruce_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_spruce_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_spruce_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_birch_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_birch_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_birch_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_birch_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_jungle_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_jungle_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_jungle_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_jungle_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_acacia_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_acacia_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_acacia_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_acacia_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_dark_oak_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_dark_oak_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_dark_oak_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_dark_oak_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_warped_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_warped_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_warped_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_warped_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_crimson_andesite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_crimson_diorite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_crimson_granite"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "counter_crimson_blackstone"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_spruce"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_birch"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_jungle"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_acacia"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_dark_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_warped"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cupboard_crimson"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_birch"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_spruce"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_jungle"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_acacia"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_dark_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_warped"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "cabinet_crimson"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_birch"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_spruce"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_jungle"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_acacia"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_dark_oak"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_crimson"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("buildersaddition", "shelf_warped"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "storage_controller"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("functionalstorage", "storage_extension"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_maple_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_willow_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_cherry_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_wisteria_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_rosewood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_morado_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_yucca_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_kousa_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_aspen_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_grimwood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_driftwood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_river_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_poise_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_cypress_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_jacaranda_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_redbud_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "autumnity_stripped_maple_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_willow_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_cherry_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "environmental_stripped_wisteria_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_rosewood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_morado_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_yucca_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_kousa_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_aspen_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "atmospheric_stripped_grimwood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_driftwood_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "upgrade_aquatic_stripped_river_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "endergetic_stripped_poise_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "bayou_blues_stripped_cypress_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_jacaranda_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("morecfm", "abundance_stripped_redbud_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "kitchen_sink"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "oak_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "spruce_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "birch_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "jungle_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "acacia_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "dark_oak_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_oak_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_spruce_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_birch_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_jungle_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_acacia_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "stripped_dark_oak_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "white_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "orange_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "magenta_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "light_blue_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "yellow_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "lime_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "pink_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "gray_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "light_gray_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "cyan_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "purple_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "blue_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "brown_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "green_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "red_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("cfm", "black_kitchen_counter"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "controller"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "storage_link"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "controller_slave"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "controller"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "oak_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "spruce_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "birch_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "jungle_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "acacia_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "dark_oak_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "warped_trim"));
        kitchenConnectors.addOptional(ResourceLocation.fromNamespaceAndPath("storagedrawers", "crimson_trim"));
    }
}
