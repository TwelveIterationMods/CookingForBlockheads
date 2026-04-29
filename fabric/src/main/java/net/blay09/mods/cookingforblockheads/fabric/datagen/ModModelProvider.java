package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;
import static net.minecraft.client.data.models.BlockModelGenerators.*;
import static net.minecraft.client.data.models.model.TextureMapping.getBlockTexture;

public class ModModelProvider extends FabricModelProvider {

    private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.EAST, Y_ROT_90)
            .select(Direction.SOUTH, Y_ROT_180)
            .select(Direction.WEST, Y_ROT_270)
            .select(Direction.NORTH, NOP);

    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleItemModel(ModBlocks.cowJar.asBlock(), ModelLocationUtils.getModelLocation(ModBlocks.cowJar.asItem()));


        ModBlocks.cookingTables.forEach((color, cookingTable) -> {
            if (color != null) {
                final var cookingTableParent = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
                        "block/cooking_table")),
                        Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                cookingTableParent.create(cookingTable.asBlock(), TextureMapping.cube(getTerracottaByColor(color)), blockStateModelGenerator.modelOutput);
            }
            blockStateModelGenerator.createNonTemplateHorizontalBlock(cookingTable.asBlock());
        });

        ModBlocks.counters.forEach((color, counter) -> {
            if (color != null) {
                final var counterParent = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/counter")),
                        Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                TextureMapping textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                counterParent.create(counter.asBlock(), textureMapping, blockStateModelGenerator.modelOutput);

                textureMapping.putForced(TextureSlot.PARTICLE, getBlockTexture(getTerracottaByColor(color)));
                final var counterDoorTemplate = new ModelTemplate(Optional.of(id("block/counter_door")),
                        Optional.empty(), TextureSlot.ALL);
                counterDoorTemplate.createWithSuffix(counter.asBlock(), "_door", textureMapping, blockStateModelGenerator.modelOutput);
                final var counterDoorFlippedTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                        "block/counter_door_flipped")),
                        Optional.empty(), TextureSlot.ALL);
                counterDoorFlippedTemplate.createWithSuffix(counter.asBlock(), "_door_flipped", textureMapping, blockStateModelGenerator.modelOutput);
            }
            blockStateModelGenerator.createNonTemplateHorizontalBlock(counter.asBlock());
            blockStateModelGenerator.registerSimpleItemModel(counter.asBlock(), ModelLocationUtils.getModelLocation(counter.asItem()));
        });

        ModBlocks.cabinets.forEach((color, cabinet) -> {
            Identifier cabinetModel = ModelLocationUtils.getModelLocation(cabinet.asBlock());
            Identifier cabinetLargeLowerModel = ModelLocationUtils.getModelLocation(cabinet.asBlock(), "_large_lower");
            Identifier cabinetLargeUpperModel = ModelLocationUtils.getModelLocation(cabinet.asBlock(), "_large_upper");
            if (color != null) {
                final var cabinetParent = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/cabinet")),
                        Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                TextureMapping textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                cabinetModel = cabinetParent.create(cabinet.asBlock(), textureMapping, blockStateModelGenerator.modelOutput);

                final var cabinetLargeLowerParent = new ModelTemplate(Optional.of(id("block/cabinet_large_lower")),
                        Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                cabinetLargeLowerModel = cabinetLargeLowerParent.createWithSuffix(cabinet.asBlock(), "_large_lower", textureMapping, blockStateModelGenerator.modelOutput);
                final var cabinetLargeUpperParent = new ModelTemplate(Optional.of(id("block/cabinet_large_upper")),
                        Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                cabinetLargeUpperModel = cabinetLargeUpperParent.createWithSuffix(cabinet.asBlock(), "_large_upper", textureMapping, blockStateModelGenerator.modelOutput);

                textureMapping.putForced(TextureSlot.PARTICLE, getBlockTexture(getTerracottaByColor(color)));
                final var cabinetDoorTemplate = new ModelTemplate(Optional.of(id("block/cabinet_door")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetDoorTemplate.createWithSuffix(cabinet.asBlock(), "_door", textureMapping, blockStateModelGenerator.modelOutput);
                final var cabinetDoorFlippedTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                        "block/cabinet_door_flipped")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetDoorFlippedTemplate.createWithSuffix(cabinet.asBlock(), "_door_flipped", textureMapping, blockStateModelGenerator.modelOutput);

                final var cabinetLargeDoorLowerTemplate = new ModelTemplate(Optional.of(id("block/cabinet_large_door_lower")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetLargeDoorLowerTemplate.createWithSuffix(cabinet.asBlock(), "_large_door_lower", textureMapping, blockStateModelGenerator.modelOutput);
                final var cabinetLargeDoorLowerFlippedTemplate = new ModelTemplate(Optional.of(id("block/cabinet_large_door_lower_flipped")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetLargeDoorLowerFlippedTemplate.createWithSuffix(cabinet.asBlock(), "_large_door_lower_flipped", textureMapping, blockStateModelGenerator.modelOutput);
                final var cabinetLargeDoorUpperTemplate = new ModelTemplate(Optional.of(id("block/cabinet_large_door_upper")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetLargeDoorUpperTemplate.createWithSuffix(cabinet.asBlock(), "_large_door_upper", textureMapping, blockStateModelGenerator.modelOutput);
                final var cabinetLargeDoorUpperFlippedTemplate = new ModelTemplate(Optional.of(id("block/cabinet_large_door_upper_flipped")),
                        Optional.empty(), TextureSlot.ALL);
                cabinetLargeDoorUpperFlippedTemplate.createWithSuffix(cabinet.asBlock(), "_large_door_upper_flipped", textureMapping, blockStateModelGenerator.modelOutput);
            }
            blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(cabinet.asBlock()).with(PropertyDispatch.initial(CabinetBlock.MODEL_TYPE)
                    .select(CabinetBlock.CabinetModelType.SMALL, plainVariant(cabinetModel))
                    .select(CabinetBlock.CabinetModelType.LARGE_LOWER, plainVariant(cabinetLargeLowerModel))
                    .select(CabinetBlock.CabinetModelType.LARGE_UPPER, plainVariant(cabinetLargeUpperModel))
            ).with(ROTATION_HORIZONTAL_FACING));
            blockStateModelGenerator.registerSimpleItemModel(cabinet.asBlock(), ModelLocationUtils.getModelLocation(cabinet.asItem()));
        });

        final var sinkModel = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/sink");
        final var sinkModelFlipped = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/sink_flipped");
        ModBlocks.sinks.forEach((color, sink) -> {
            if (color != null) {
                final var sinkParent = new ModelTemplate(Optional.of(sinkModel), Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                final var sinkFlippedParent = new ModelTemplate(Optional.of(sinkModelFlipped), Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                final var textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                final var dyedSinkModel = sinkParent.create(sink.asBlock(), textureMapping, blockStateModelGenerator.modelOutput);
                final var dyedSinkModelFlipped = sinkFlippedParent.createWithSuffix(sink.asBlock(), "_flipped", textureMapping, blockStateModelGenerator.modelOutput);
                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(sink.asBlock())
                        .with(createBooleanModelDispatch(SinkBlock.FLIPPED, plainVariant(dyedSinkModelFlipped), plainVariant(dyedSinkModel)))
                        .with(ROTATION_HORIZONTAL_FACING));
            } else {
                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.sinks.get(null).asBlock())
                        .with(createBooleanModelDispatch(SinkBlock.FLIPPED, plainVariant(sinkModelFlipped), plainVariant(sinkModel)))
                        .with(ROTATION_HORIZONTAL_FACING));
            }
        });

        final var chickenSinkModel = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/chicken_sink");
        final var chickenSinkModelFlipped = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/chicken_sink_flipped");
        ModBlocks.chickenSinks.forEach((color, sink) -> {
            if (color != null) {
                final var sinkParent = new ModelTemplate(Optional.of(chickenSinkModel), Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                final var sinkFlippedParent = new ModelTemplate(Optional.of(chickenSinkModelFlipped), Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
                final var textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                final var dyedSinkModel = sinkParent.create(sink.asBlock(), textureMapping, blockStateModelGenerator.modelOutput);
                final var dyedSinkModelFlipped = sinkFlippedParent.createWithSuffix(sink.asBlock(), "_flipped", textureMapping, blockStateModelGenerator.modelOutput);
                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(sink.asBlock())
                        .with(createBooleanModelDispatch(ChickenSinkBlock.FLIPPED, plainVariant(dyedSinkModelFlipped), plainVariant(dyedSinkModel)))
                        .with(ROTATION_HORIZONTAL_FACING));
            } else {
                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.chickenSinks.get(null).asBlock())
                        .with(createBooleanModelDispatch(ChickenSinkBlock.FLIPPED, plainVariant(chickenSinkModelFlipped), plainVariant(chickenSinkModel)))
                        .with(ROTATION_HORIZONTAL_FACING));
            }
            blockStateModelGenerator.registerSimpleTintedItemModel(sink.asBlock(), ModelLocationUtils.getModelLocation(sink.asBlock()), new Constant(0xff3f76e4));
        });

        ModBlocks.ovens.forEach((color, oven) -> createOvenBlock(blockStateModelGenerator, oven.asBlock(), color));

        final var toasterModel = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/toaster");
        final var toasterModelActive = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/toaster_active");
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.toaster.asBlock())
                .with(createBooleanModelDispatch(ToasterBlock.ACTIVE, plainVariant(toasterModelActive), plainVariant(toasterModel)))
                .with(ROTATION_HORIZONTAL_FACING));
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.coffeeMachine.asBlock());

        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.milkJar.asBlock());
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.cowJar.asBlock());
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.cookieJar.asBlock());
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.fruitBasket.asBlock());
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.cuttingBoard.asBlock());
        ModBlocks.connectors.forEach((color, connector) -> createConnector(blockStateModelGenerator, connector.asBlock(), color));

        final var kitchenFloorParent = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/kitchen_floor")),
                Optional.empty(), TextureSlot.ALL, TextureSlot.PARTICLE);
        ModBlocks.kitchenFloors.forEach((color, kitchenFloor) -> {
            kitchenFloorParent.create(kitchenFloor.asBlock(), TextureMapping.cube(kitchenFloor.asBlock()), blockStateModelGenerator.modelOutput);
            blockStateModelGenerator.createNonTemplateModelBlock(kitchenFloor.asBlock());
        });

        final var fridgeParentSmall = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "block/fridge")),
                Optional.empty(), TextureSlot.PARTICLE);
        final var fridgeParentLargeLower = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
                "block/fridge_large_lower")),
                Optional.empty(), TextureSlot.PARTICLE);
        final var fridgeParentLargeUpper = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID,
                "block/fridge_large_upper")),
                Optional.empty(), TextureSlot.PARTICLE);
        ModBlocks.fridges.forEach((color, fridge) -> {
            final var textureMapping = getFridgeTextures(color);
            final var fridgeModelSmall = fridgeParentSmall.create(fridge.asBlock(), textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeModelLargeLower = fridgeParentLargeLower.createWithSuffix(fridge.asBlock(),
                    "_large_lower",
                    textureMapping,
                    blockStateModelGenerator.modelOutput);
            final var fridgeModelLargeUpper = fridgeParentLargeUpper.createWithSuffix(fridge.asBlock(),
                    "_large_upper",
                    textureMapping,
                    blockStateModelGenerator.modelOutput);
            blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(fridge.asBlock()).with(PropertyDispatch.initial(FridgeBlock.MODEL_TYPE)
                    .select(FridgeBlock.FridgeModelType.SMALL, plainVariant(fridgeModelSmall))
                    .select(FridgeBlock.FridgeModelType.LARGE_LOWER, plainVariant(fridgeModelLargeLower))
                    .select(FridgeBlock.FridgeModelType.LARGE_UPPER, plainVariant(fridgeModelLargeUpper))
            ).with(ROTATION_HORIZONTAL_FACING));
            blockStateModelGenerator.registerSimpleItemModel(fridge.asBlock(), ModelLocationUtils.getModelLocation(fridge.asItem()));

            final var fridgeDoorTemplate = new ModelTemplate(Optional.of(id("block/fridge_door")),
                    Optional.empty());
            fridgeDoorTemplate.createWithSuffix(fridge.asBlock(), "_door", textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeDoorFlippedTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                    "block/fridge_door_flipped")),
                    Optional.empty());
            fridgeDoorFlippedTemplate.createWithSuffix(fridge.asBlock(), "_door_flipped", textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeLargeDoorLowerTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                    "block/fridge_large_door_lower")),
                    Optional.empty());
            fridgeLargeDoorLowerTemplate.createWithSuffix(fridge.asBlock(), "_large_door_lower", textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeLargeDoorLowerFlippedTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                    "block/fridge_large_door_lower_flipped")),
                    Optional.empty());
            fridgeLargeDoorLowerFlippedTemplate.createWithSuffix(fridge.asBlock(), "_large_door_lower_flipped", textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeLargeDoorUpperTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                    "block/fridge_large_door_upper")),
                    Optional.empty());
            fridgeLargeDoorUpperTemplate.createWithSuffix(fridge.asBlock(), "_large_door_upper", textureMapping, blockStateModelGenerator.modelOutput);
            final var fridgeLargeDoorUpperFlippedTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                    "block/fridge_large_door_upper_flipped")),
                    Optional.empty());
            fridgeLargeDoorUpperFlippedTemplate.createWithSuffix(fridge.asBlock(), "_large_door_upper_flipped", textureMapping, blockStateModelGenerator.modelOutput);
        });

        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.toolRack.asBlock());
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.spiceRack.asBlock());

        final var ovenTemplate = new ModelTemplate(Optional.of(id("item/oven")), Optional.empty());
        ModBlocks.ovens.forEach((color, oven) -> {
            final var modelLocation = ModelLocationUtils.getModelLocation(oven.asItem());
            final var textureMapping = getOvenTextures(color, false);
            ovenTemplate.create(modelLocation, textureMapping, blockStateModelGenerator.modelOutput);
        });

        final var fridgeTemplate = new ModelTemplate(Optional.of(id("item/fridge")),
                Optional.empty());
        ModBlocks.fridges.forEach((color, fridge) -> {
            final var modelLocation = ModelLocationUtils.getModelLocation(fridge.asItem());
            fridgeTemplate.create(modelLocation, getFridgeTextures(color), blockStateModelGenerator.modelOutput);
        });

        final var counterTemplate = new ModelTemplate(Optional.of(id("item/counter")),
                Optional.empty(),
                TextureSlot.ALL);
        ModBlocks.counters.forEach((color, counter) -> {
            if (color != null) {
                final var modelLocation = ModelLocationUtils.getModelLocation(counter.asItem());
                final var textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                counterTemplate.create(modelLocation, textureMapping, blockStateModelGenerator.modelOutput);
            }
        });

        final var cabinetTemplate = new ModelTemplate(Optional.of(id("item/cabinet")),
                Optional.empty(),
                TextureSlot.ALL);
        ModBlocks.cabinets.forEach((color, cabinet) -> {
            if (color != null) {
                final var modelLocation = ModelLocationUtils.getModelLocation(cabinet.asItem());
                final var textureMapping = TextureMapping.cube(getTerracottaByColor(color));
                cabinetTemplate.create(modelLocation, textureMapping, blockStateModelGenerator.modelOutput);
            }
        });
    }

    private Block getTerracottaByColor(@Nullable DyeColor color) {
        if (color == null) {
            return Blocks.TERRACOTTA;
        }
        return switch (color) {
            case WHITE -> Blocks.DYED_TERRACOTTA.white();
            case ORANGE -> Blocks.DYED_TERRACOTTA.orange();
            case MAGENTA -> Blocks.DYED_TERRACOTTA.magenta();
            case LIGHT_BLUE -> Blocks.DYED_TERRACOTTA.lightBlue();
            case YELLOW -> Blocks.DYED_TERRACOTTA.yellow();
            case LIME -> Blocks.DYED_TERRACOTTA.lime();
            case PINK -> Blocks.DYED_TERRACOTTA.pink();
            case GRAY -> Blocks.DYED_TERRACOTTA.gray();
            case LIGHT_GRAY -> Blocks.DYED_TERRACOTTA.lightGray();
            case CYAN -> Blocks.DYED_TERRACOTTA.cyan();
            case PURPLE -> Blocks.DYED_TERRACOTTA.purple();
            case BLUE -> Blocks.DYED_TERRACOTTA.blue();
            case BROWN -> Blocks.DYED_TERRACOTTA.brown();
            case GREEN -> Blocks.DYED_TERRACOTTA.green();
            case RED -> Blocks.DYED_TERRACOTTA.red();
            case BLACK -> Blocks.DYED_TERRACOTTA.black();
        };
    }

    private void createConnector(BlockModelGenerators blockStateModelGenerator, Block block, @Nullable DyeColor color) {
        final var innerModelBottomTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_inner_bottom_template")),
                Optional.of("_inner_bottom"));
        final var straightModelBottomTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_straight_bottom_template")),
                Optional.of("_straight_bottom"));
        final var outerModelBottomTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_outer_bottom_template")),
                Optional.of("_outer_bottom"));
        final var innerModelTopTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_inner_top_template")),
                Optional.of("_inner_top"));
        final var straightModelTopTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_straight_top_template")),
                Optional.of("_straight_top"));
        final var outerModelTopTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/connector_outer_top_template")),
                Optional.of("_outer_top"));

        final var textures = new TextureMapping();
        if (color != null) {
            textures.putForced(TextureSlot.PARTICLE, new Material(Identifier.withDefaultNamespace("block/" + color.getSerializedName() + "_terracotta")));
            textures.putForced(TextureSlot.TEXTURE, new Material(Identifier.withDefaultNamespace("block/" + color.getSerializedName() + "_terracotta")));
        }

        final var straightModelBottom = straightModelBottomTemplate.create(block, textures, blockStateModelGenerator.modelOutput);
        blockStateModelGenerator.blockStateOutput.accept(createStairLike(block,
                plainVariant(innerModelBottomTemplate.create(block, textures, blockStateModelGenerator.modelOutput)),
                plainVariant(straightModelBottom),
                plainVariant(outerModelBottomTemplate.create(block, textures, blockStateModelGenerator.modelOutput)),
                plainVariant(innerModelTopTemplate.create(block, textures, blockStateModelGenerator.modelOutput)),
                plainVariant(straightModelTopTemplate.create(block, textures, blockStateModelGenerator.modelOutput)),
                plainVariant(outerModelTopTemplate.create(block, textures, blockStateModelGenerator.modelOutput))));

        blockStateModelGenerator.registerSimpleItemModel(block, straightModelBottom);
    }

    public static MultiVariantGenerator createStairLike(Block block, MultiVariant innerModelBottom, MultiVariant straightModelBottom, MultiVariant outerModelBottom, MultiVariant innerModelTop, MultiVariant straightModelTop, MultiVariant outerModelTop) {
        return MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                straightModelBottom.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                straightModelBottom.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                straightModelBottom.with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.STRAIGHT,
                                straightModelBottom.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, outerModelBottom)
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                outerModelBottom.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                outerModelBottom.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_RIGHT,
                                outerModelBottom.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                outerModelBottom.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                outerModelBottom.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, outerModelBottom)
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.OUTER_LEFT,
                                outerModelBottom.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, innerModelBottom)
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                innerModelBottom.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                innerModelBottom.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.INNER_RIGHT,
                                innerModelBottom.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                innerModelBottom.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                innerModelBottom.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, innerModelBottom)
                        .select(Direction.NORTH,
                                Half.BOTTOM,
                                StairsShape.INNER_LEFT,
                                innerModelBottom.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                straightModelTop.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                straightModelTop.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                straightModelTop.with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.STRAIGHT,
                                straightModelTop.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                outerModelTop.with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                outerModelTop.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                outerModelTop.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.OUTER_RIGHT,
                                outerModelTop.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                outerModelTop.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                outerModelTop.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                outerModelTop.with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.OUTER_LEFT,
                                outerModelTop.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                innerModelTop.with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                innerModelTop.with(Y_ROT_180).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                innerModelTop.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.INNER_RIGHT,
                                innerModelTop.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.EAST,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                innerModelTop.with(Y_ROT_270).with(UV_LOCK))
                        .select(Direction.WEST,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                innerModelTop.with(Y_ROT_90).with(UV_LOCK))
                        .select(Direction.SOUTH,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                innerModelTop.with(UV_LOCK))
                        .select(Direction.NORTH,
                                Half.TOP,
                                StairsShape.INNER_LEFT,
                                innerModelTop.with(Y_ROT_180).with(UV_LOCK)));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.recipeBook.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.craftingBook.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.noFilterBook.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.heatingUnit.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.iceUnit.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.saltFilter.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.iceCubes.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.preservationChamber.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.chefHat.asItem(), ModelTemplates.FLAT_ITEM);
    }

    private void createOvenBlock(BlockModelGenerators blockStateModelGenerator, Block block, DyeColor color) {
        final var ovenTemplate = new ModelTemplate(Optional.of(id("block/oven")), Optional.empty());
        final var textureMapping = getOvenTextures(color, false);
        final var ovenModel = ovenTemplate.create(block, textureMapping, blockStateModelGenerator.modelOutput);
        final var activeTextureMapping = getOvenTextures(color, true);
        final var activeOvenModel = ovenTemplate.createWithSuffix(block, "_active", activeTextureMapping, blockStateModelGenerator.modelOutput);
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(createBooleanModelDispatch(OvenBlock.ACTIVE, plainVariant(ovenModel), plainVariant(activeOvenModel)))
                .with(ROTATION_HORIZONTAL_FACING));
        blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block.asItem()));

        final var ovenDoorTemplate = new ModelTemplate(Optional.of(id("block/oven_door")),
                Optional.empty());
        ovenDoorTemplate.createWithSuffix(block, "_door", textureMapping, blockStateModelGenerator.modelOutput);
        final var ovenDoorActiveTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/oven_door_active")),
                Optional.empty());
        ovenDoorActiveTemplate.createWithSuffix(block, "_door_active", activeTextureMapping, blockStateModelGenerator.modelOutput);
        final var ovenDoorHandleTemplate = new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath("cookingforblockheads",
                "block/oven_door_handle")),
                Optional.empty());
        ovenDoorHandleTemplate.createWithSuffix(block, "_door_handle", activeTextureMapping, blockStateModelGenerator.modelOutput);
    }

    private static TextureMapping getOvenTextures(DyeColor color, boolean active) {
        final var textureMapping = new TextureMapping();
        final var colorName = color.getName();
        textureMapping.putForced(TextureSlot.PARTICLE, new Material(id("block/" + colorName + "_oven_side")));
        textureMapping.putForced(TextureSlot.TEXTURE, new Material(id("block/" + colorName + "_oven_side")));
        if (active) {
            textureMapping.putForced(TextureSlot.create("ovenfront"), new Material(id("block/" + color.getName() + "_oven_front_active")));
        } else {
            textureMapping.putForced(TextureSlot.create("ovenfront"), new Material(id("block/" + colorName + "_oven_front")));
        }
        textureMapping.putForced(TextureSlot.create("ovenfront_active"), new Material(id("block/" + colorName + "_oven_front_active")));
        textureMapping.putForced(TextureSlot.create("oventop"), new Material(id("block/" + colorName + "_oven_top")));
        textureMapping.putForced(TextureSlot.create("ovenbottom"), new Material(id("block/" + colorName + "_oven_bottom")));
        textureMapping.putForced(TextureSlot.create("backsplash"), new Material(id("block/" + colorName + "_oven_side")));
        return textureMapping;
    }

    private static TextureMapping getFridgeTextures(DyeColor color) {
        final var textureMapping = new TextureMapping();
        final var colorName = color.getName();
        textureMapping.put(TextureSlot.PARTICLE, new Material(id("block/" + colorName + "_fridge_side")));
        textureMapping.putForced(TextureSlot.create("fridge_back"), new Material(id("block/" + colorName + "_fridge_back")));
        textureMapping.putForced(TextureSlot.create("fridge_top"), new Material(id("block/" + colorName + "_fridge_top")));
        textureMapping.putForced(TextureSlot.create("fridge_side"), new Material(id("block/" + colorName + "_fridge_side")));
        textureMapping.putForced(TextureSlot.create("fridge_inside"), new Material(id("block/" + colorName + "_fridge_inside")));
        textureMapping.putForced(TextureSlot.create("particle"), new Material(id("block/" + colorName + "_fridge_side")));
        return textureMapping;
    }

}
