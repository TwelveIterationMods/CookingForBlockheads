package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

import static net.minecraft.data.models.BlockModelGenerators.createHorizontalFacingDispatch;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.cabinet);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.counter);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.cowJar);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.fridge);

        for (final var oven : ModBlocks.ovens) {
            createOvenBlock(blockStateModelGenerator, oven);
        }
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.toaster);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.milkJar);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cowJar);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.fruitBasket);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cuttingBoard);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cookingTable);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.fridge);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.sink);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.counter);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cabinet);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.corner);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.hangingCorner);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            blockStateModelGenerator.createNonTemplateModelBlock(kitchenFloor);
        }

        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.toolRack);
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.spiceRack);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.recipeBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.craftingBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.noFilterBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.heatingUnit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.iceUnit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.preservationChamber, ModelTemplates.FLAT_ITEM);

        final var ovenTemplate = new ModelTemplate(Optional.of(new ResourceLocation("cookingforblockheads", "item/dyed_oven")), Optional.empty());
        for (final var oven : ModBlocks.ovens) {
            final var modelLocation = ModelLocationUtils.getModelLocation(oven.asItem());
            final var textureMapping = new TextureMapping();
            final var colorName = oven.getColor().getName();
            textureMapping.putForced(TextureSlot.PARTICLE, new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
            textureMapping.putForced(TextureSlot.TEXTURE, new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
            textureMapping.putForced(TextureSlot.create("ovenfront"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_front"));
            textureMapping.putForced(TextureSlot.create("ovenfront_active"),
                    new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_front_active"));
            textureMapping.putForced(TextureSlot.create("oventop"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_top"));
            textureMapping.putForced(TextureSlot.create("ovenbottom"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_bottom"));
            textureMapping.putForced(TextureSlot.create("backsplash"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
            ovenTemplate.create(modelLocation, textureMapping, itemModelGenerator.output);
        }
    }

    private void createOvenBlock(BlockModelGenerators blockStateModelGenerator, Block block) {
        final var dispatch = PropertyDispatch.properties(OvenBlock.ACTIVE, OvenBlock.POWERED);
        for (var active = 0; active <= 1; active++) {
            for (var powered = 0; powered <= 1; powered++) {
                var suffix = "";
                if (powered == 1) {
                    suffix += "_powered";
                }
                if (active == 1) {
                    suffix += "_active";
                }
                final var modelLocation = ModelLocationUtils.getModelLocation(block, suffix);
                dispatch.select(active == 1, powered == 1, Variant.variant().with(VariantProperties.MODEL, modelLocation));
            }
        }
        final var generator = MultiVariantGenerator.multiVariant(block)
                .with(createHorizontalFacingDispatch())
                .with(dispatch);
        blockStateModelGenerator.blockStateOutput.accept(generator);
        blockStateModelGenerator.skipAutoItemBlock(block);
    }

}
