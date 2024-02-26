package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "mineable/pickaxe"));
        final var mineablePickaxeBuilder = getOrCreateTagBuilder(mineablePickaxeTag);
        mineablePickaxeBuilder.add(ModBlocks.cookingTable,
                ModBlocks.fridge,
                ModBlocks.sink,
                ModBlocks.counter,
                ModBlocks.cabinet,
                ModBlocks.connector,
                ModBlocks.toaster,
                ModBlocks.milkJar,
                ModBlocks.cowJar);
        for (final var oven : ModBlocks.ovens) {
            mineablePickaxeBuilder.add(oven);
        }
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            mineablePickaxeBuilder.add(kitchenFloor);
        }

        final var mineableAxeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "mineable/axe"));
        final var mineableAxeBuilder = getOrCreateTagBuilder(mineableAxeTag);
        mineableAxeBuilder.add(ModBlocks.toolRack, ModBlocks.spiceRack, ModBlocks.fruitBasket, ModBlocks.cuttingBoard);

        final var isDyeable = getOrCreateTagBuilder(ModBlockTags.IS_DYEABLE);
        isDyeable.add(ModBlocks.cookingTable,
                ModBlocks.fridge,
                ModBlocks.sink,
                ModBlocks.counter,
                ModBlocks.cabinet,
                ModBlocks.connector);

        getOrCreateTagBuilder(ModBlockTags.OVENS).add(ModBlocks.ovens);
        final var dyedOvens = getOrCreateTagBuilder(ModBlockTags.DYED_OVENS);
        for (final var oven : ModBlocks.ovens) {
            if (oven.getColor() != DyeColor.WHITE) {
                dyedOvens.add(oven);
            }
        }

        final var kitchenConnectors = getOrCreateTagBuilder(ModBlockTags.KITCHEN_CONNECTORS);
        kitchenConnectors.add(ModBlocks.connector);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            kitchenConnectors.add(kitchenFloor);
        }
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_oak_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_oak_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_oak_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_oak_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_spruce_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_spruce_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_spruce_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_spruce_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_birch_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_birch_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_birch_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_birch_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_jungle_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_jungle_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_jungle_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_jungle_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_acacia_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_acacia_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_acacia_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_acacia_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_dark_oak_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_dark_oak_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_dark_oak_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_dark_oak_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_warped_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_warped_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_warped_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_warped_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_crimson_andesite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_crimson_diorite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_crimson_granite"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "counter_crimson_blackstone"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_spruce"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_birch"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_jungle"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_acacia"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_dark_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_warped"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cupboard_crimson"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_birch"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_spruce"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_jungle"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_acacia"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_dark_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_warped"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "cabinet_crimson"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_birch"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_spruce"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_jungle"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_acacia"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_dark_oak"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_crimson"));
        kitchenConnectors.addOptional(new ResourceLocation("buildersaddition", "shelf_warped"));
        kitchenConnectors.addOptional(new ResourceLocation("functionalstorage", "storage_controller"));
        kitchenConnectors.addOptional(new ResourceLocation("functionalstorage", "storage_extension"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "autumnity_maple_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_willow_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_cherry_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_wisteria_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_rosewood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_morado_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_yucca_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_kousa_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_aspen_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_grimwood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "upgrade_aquatic_driftwood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "upgrade_aquatic_river_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "endergetic_poise_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "bayou_blues_cypress_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "abundance_jacaranda_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "abundance_redbud_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "autumnity_stripped_maple_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_stripped_willow_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_stripped_cherry_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "environmental_stripped_wisteria_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_rosewood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_morado_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_yucca_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_kousa_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_aspen_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "atmospheric_stripped_grimwood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "upgrade_aquatic_stripped_driftwood_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "upgrade_aquatic_stripped_river_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "endergetic_stripped_poise_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "bayou_blues_stripped_cypress_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "abundance_stripped_jacaranda_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("morecfm", "abundance_stripped_redbud_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "kitchen_sink"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "oak_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "spruce_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "birch_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "jungle_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "acacia_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "dark_oak_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_oak_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_spruce_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_birch_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_jungle_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_acacia_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "stripped_dark_oak_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "white_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "orange_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "magenta_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "light_blue_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "yellow_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "lime_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "pink_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "gray_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "light_gray_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "cyan_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "purple_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "blue_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "brown_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "green_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "red_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("cfm", "black_kitchen_counter"));
        kitchenConnectors.addOptional(new ResourceLocation("sophisticatedstorage", "controller"));
        kitchenConnectors.addOptional(new ResourceLocation("sophisticatedstorage", "storage_link"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "controller_slave"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "controller"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "oak_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "spruce_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "birch_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "jungle_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "acacia_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "dark_oak_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "warped_trim"));
        kitchenConnectors.addOptional(new ResourceLocation("storagedrawers", "crimson_trim"));
    }
}
