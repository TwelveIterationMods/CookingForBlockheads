package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.OvenBlock;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.minecraft.data.models.BlockModelGenerators.createBooleanModelDispatch;
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
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            blockStateModelGenerator.createNonTemplateModelBlock(kitchenFloor);
        }
        createStairLike(blockStateModelGenerator, ModBlocks.connector);

        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.toolRack);
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.spiceRack);
    }

    private void createStairLike(BlockModelGenerators blockStateModelGenerator, Block block) {
        final var innerModelBottom = new ResourceLocation("cookingforblockheads", "block/connector_inner_bottom");
        final var straightModelBottom = new ResourceLocation("cookingforblockheads", "block/connector_straight_bottom");
        final var outerModelBottom = new ResourceLocation("cookingforblockheads", "block/connector_outer_bottom");
        final var innerModelTop = new ResourceLocation("cookingforblockheads", "block/connector_inner_top");
        final var straightModelTop = new ResourceLocation("cookingforblockheads", "block/connector_straight_top");
        final var outerModelTop = new ResourceLocation("cookingforblockheads", "block/connector_outer_top");

        blockStateModelGenerator.blockStateOutput.accept(createStairLike(block,
                innerModelBottom,
                straightModelBottom,
                outerModelBottom,
                innerModelTop,
                straightModelTop,
                outerModelTop));
        blockStateModelGenerator.delegateItemModel(block, straightModelBottom);
    }

    public static BlockStateGenerator createStairLike(Block block, ResourceLocation innerModelBottom, ResourceLocation straightModelBottom, ResourceLocation outerModelBottom, ResourceLocation innerModelTop, ResourceLocation straightModelTop, ResourceLocation outerModelTop) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, outerModelBottom))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, outerModelBottom))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, innerModelBottom))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, innerModelBottom))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelBottom)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, straightModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, outerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                Variant.variant()
                                        .with(VariantProperties.MODEL, innerModelTop)
                                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                        .with(VariantProperties.UV_LOCK, true)));
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
            final var textureMapping = getOvenTextures(oven, false);
            ovenTemplate.create(modelLocation, textureMapping, itemModelGenerator.output);
        }
    }

    private void createOvenBlock(BlockModelGenerators blockStateModelGenerator, OvenBlock block) {
        final var ovenTemplate = new ModelTemplate(Optional.of(new ResourceLocation("cookingforblockheads", "block/dyed_oven")), Optional.empty());
        final var textureMapping = getOvenTextures(block, false);
        final var ovenModel = ovenTemplate.create(block, textureMapping, blockStateModelGenerator.modelOutput);
        final var activeTextureMapping = getOvenTextures(block, true);
        final var activeOvenModel = ovenTemplate.createWithSuffix(block, "_active", activeTextureMapping, blockStateModelGenerator.modelOutput);
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(createBooleanModelDispatch(OvenBlock.ACTIVE, ovenModel, activeOvenModel))
                .with(createHorizontalFacingDispatch()));
        blockStateModelGenerator.skipAutoItemBlock(block);

        final var ovenDoorTemplate = new ModelTemplate(Optional.of(new ResourceLocation("cookingforblockheads", "block/dyed_oven_door")), Optional.empty());
        ovenDoorTemplate.createWithSuffix(block, "_door", textureMapping, blockStateModelGenerator.modelOutput);
        final var ovenDoorActiveTemplate = new ModelTemplate(Optional.of(new ResourceLocation("cookingforblockheads", "block/dyed_oven_door_active")),
                Optional.empty());
        ovenDoorActiveTemplate.createWithSuffix(block, "_door_active", activeTextureMapping, blockStateModelGenerator.modelOutput);
    }

    @NotNull
    private static TextureMapping getOvenTextures(OvenBlock oven, boolean active) {
        final var textureMapping = new TextureMapping();
        final var colorName = oven.getColor().getName();
        textureMapping.putForced(TextureSlot.PARTICLE, new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
        textureMapping.putForced(TextureSlot.TEXTURE, new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
        if (active) {
            textureMapping.putForced(TextureSlot.create("ovenfront"),
                    new ResourceLocation("cookingforblockheads", "block/" + oven.getColor().getName() + "_oven_front_active"));
        } else {
            textureMapping.putForced(TextureSlot.create("ovenfront"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_front"));
        }
        textureMapping.putForced(TextureSlot.create("ovenfront_active"),
                new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_front_active"));
        textureMapping.putForced(TextureSlot.create("oventop"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_top"));
        textureMapping.putForced(TextureSlot.create("ovenbottom"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_bottom"));
        textureMapping.putForced(TextureSlot.create("backsplash"), new ResourceLocation("cookingforblockheads", "block/" + colorName + "_oven_side"));
        return textureMapping;
    }

}
