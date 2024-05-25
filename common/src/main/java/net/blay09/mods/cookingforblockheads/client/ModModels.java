package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.math.Axis;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Supplier;

public class ModModels {
    public static DeferredObject<BakedModel> milkJarLiquid;
    public static DeferredObject<BakedModel> cowJarLiquid;
    public static DeferredObject<BakedModel> sinkLiquid;
    public static List<DeferredObject<BakedModel>> ovenDoors;
    public static List<DeferredObject<BakedModel>> ovenDoorHandles;
    public static List<DeferredObject<BakedModel>> ovenDoorsActive;
    public static DeferredObject<BakedModel> fridgeDoor;
    public static DeferredObject<BakedModel> fridgeDoorFlipped;
    public static DeferredObject<BakedModel> fridgeDoorLargeLower;
    public static DeferredObject<BakedModel> fridgeDoorLargeUpper;
    public static DeferredObject<BakedModel> fridgeDoorLargeLowerFlipped;
    public static DeferredObject<BakedModel> fridgeDoorLargeUpperFlipped;
    public static List<DeferredObject<BakedModel>> counterDoors;
    public static List<DeferredObject<BakedModel>> counterDoorsFlipped;
    public static List<DeferredObject<BakedModel>> cabinetDoors;
    public static List<DeferredObject<BakedModel>> cabinetDoorsFlipped;

    public static void initialize(BalmModels models) {
        DyeColor[] colors = DyeColor.values();

        milkJarLiquid = models.loadModel(id("block/milk_jar_liquid"));
        cowJarLiquid = models.loadModel(id("block/cow_jar_liquid"));
        sinkLiquid = models.loadModel(id("block/sink_liquid"));
        ovenDoors = new ArrayList<>(colors.length);
        ovenDoorHandles = new ArrayList<>(colors.length);
        ovenDoorsActive = new ArrayList<>(colors.length);
        models.loadModel(id("block/dyed_oven_door_active"));
        for (DyeColor color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            final var colorPrefixExceptWhite = color == DyeColor.WHITE ? "" : colorPrefix;
            ovenDoors.add(color.getId(), models.loadModel(id("block/" + colorPrefixExceptWhite + "oven_door")));
            ovenDoorsActive.add(color.getId(), models.loadModel(id("block/" + colorPrefixExceptWhite + "oven_door_active")));
            ovenDoorHandles.add(color.getId(), models.loadModel(id("block/oven_door_handle")));
        }
        fridgeDoor = models.loadModel(id("block/fridge_door"));
        fridgeDoorFlipped = models.loadModel(id("block/fridge_door_flipped"));
        fridgeDoorLargeLower = models.loadModel(id("block/fridge_large_door_lower"));
        fridgeDoorLargeLowerFlipped = models.loadModel(id("block/fridge_large_door_lower_flipped"));
        fridgeDoorLargeUpper = models.loadModel(id("block/fridge_large_door_upper"));
        fridgeDoorLargeUpperFlipped = models.loadModel(id("block/fridge_large_door_upper_flipped"));

        counterDoors = new ArrayList<>(colors.length + 1);
        counterDoors.add(0, models.loadModel(id("block/counter_door")));
        counterDoorsFlipped = new ArrayList<>(colors.length + 1);
        counterDoorsFlipped.add(0, models.loadModel(id("block/counter_door_flipped")));
        for (DyeColor color : colors) {
            counterDoors.add(color.getId() + 1, models.retexture(id("block/counter_door"), replaceTexture(getColoredTerracottaTexture(color))));
            counterDoorsFlipped.add(color.getId() + 1, models.retexture(id("block/counter_door_flipped"), replaceTexture(getColoredTerracottaTexture(color))));
        }

        cabinetDoors = Lists.newArrayListWithCapacity(colors.length + 1);
        cabinetDoors.add(0, models.loadModel(id("block/cabinet_door")));
        cabinetDoorsFlipped = Lists.newArrayListWithCapacity(colors.length + 1);
        cabinetDoorsFlipped.add(0, models.loadModel(id("block/cabinet_door_flipped")));
        for (DyeColor color : colors) {
            cabinetDoors.add(color.getId() + 1, models.retexture(id("block/cabinet_door"), replaceTexture(getColoredTerracottaTexture(color))));
            cabinetDoorsFlipped.add(color.getId() + 1, models.retexture(id("block/cabinet_door_flipped"), replaceTexture(getColoredTerracottaTexture(color))));
        }

        ResourceLocation sinkModel = id("block/sink");
        ResourceLocation sinkFlippedModel = id("block/sink_flipped");
        models.overrideModel(() -> ModBlocks.sink,
                models.loadDynamicModel(id("block/sink"), Set.of(sinkModel, sinkFlippedModel), it -> it.getValue(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel, it -> {
                    if (it.getValue(SinkBlock.HAS_COLOR)) {
                        return replaceTexture(getColoredTerracottaTexture(it.getValue(SinkBlock.COLOR)));
                    }

                    return Collections.emptyMap();
                }, ModModels::lowerableFacingTransforms)::get);

        ResourceLocation toasterModel = id("block/toaster");
        ResourceLocation toasterActiveModel = id("block/toaster_active");
        models.overrideModel(() -> ModBlocks.toaster,
                models.loadDynamicModel(id("block/toaster"),
                        Set.of(toasterModel, toasterActiveModel),
                        it -> it.getValue(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel,
                        null,
                        ModModels::lowerableFacingTransforms)::get);

        ResourceLocation fridgeSmallModel = id("block/fridge");
        ResourceLocation fridgeLargeLowerModel = id("block/fridge_large_lower");
        ResourceLocation fridgeLargeUpperModel = id("block/fridge_large_upper");
        models.overrideModel(() -> ModBlocks.fridge, models.loadDynamicModel(id("block/fridge"), Set.of(fridgeSmallModel, fridgeLargeLowerModel, fridgeLargeUpperModel), it -> {
            FridgeBlock.FridgeModelType fridgeModelType = it.getValue(FridgeBlock.MODEL_TYPE);
            return switch (fridgeModelType) {
                case LARGE_LOWER -> fridgeLargeLowerModel;
                case LARGE_UPPER -> fridgeLargeUpperModel;
                default -> fridgeSmallModel;
            };
        }, null, ModModels::lowerableFacingTransforms)::get);

        models.overrideModel(() -> ModBlocks.cuttingBoard, createLowerableFacingModel("block/cutting_board")::get);
        models.overrideModel(() -> ModBlocks.fruitBasket, createLowerableFacingModel("block/fruit_basket")::get);
        models.overrideModel(() -> ModBlocks.milkJar, createLowerableFacingModel("block/milk_jar", List.of(RenderType.cutout()))::get);
        models.overrideModel(() -> ModBlocks.cowJar, createLowerableFacingModel("block/milk_jar", List.of(RenderType.cutout()))::get);

        registerColoredKitchenBlock(BalmClient.getModels(), () -> ModBlocks.cookingTable, "block/cooking_table");
        registerColoredKitchenBlock(BalmClient.getModels(), () -> ModBlocks.counter, "block/counter");
        registerColoredKitchenBlock(BalmClient.getModels(), () -> ModBlocks.cabinet, "block/cabinet");
    }

    private static DeferredObject<BakedModel> createLowerableFacingModel(String modelPath) {
        return createLowerableFacingModel(modelPath, Collections.emptyList());
    }

    private static DeferredObject<BakedModel> createLowerableFacingModel(String modelPath, List<RenderType> renderTypes) {
        final var modelId = id(modelPath);
        return BalmClient.getModels().loadDynamicModel(modelId, Set.of(modelId), null, null, ModModels::lowerableFacingTransforms, renderTypes);
    }

    private static void registerColoredKitchenBlock(BalmModels models, Supplier<Block> blockSupplier, String modelPath) {
        final var modelId = id(modelPath);
        models.overrideModel(blockSupplier, models.loadDynamicModel(modelId, Set.of(modelId), null, it -> {
            if (it.getValue(BaseKitchenBlock.HAS_COLOR)) {
                return replaceTexture(getColoredTerracottaTexture(it.getValue(BaseKitchenBlock.COLOR)));
            }

            return Collections.emptyMap();
        }, ModModels::lowerableFacingTransforms)::get);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, path);
    }

    private static ImmutableMap<String, String> replaceTexture(String texturePath) {
        return ImmutableMap.<String, String>builder().put("texture", texturePath).put("particle", texturePath).build();
    }

    private static String getColoredTerracottaTexture(DyeColor color) {
        return "minecraft:block/" + color.name().toLowerCase(Locale.ENGLISH) + "_terracotta";
    }

    private static void lowerableFacingTransforms(BlockState state, Matrix4f transform) {
        if (state.hasProperty(BaseKitchenBlock.LOWERED) && state.getValue(BaseKitchenBlock.LOWERED)) {
            transform.translate(new Vector3f(0, -0.05f, 0f));
        }

        if (state.hasProperty(BaseKitchenBlock.FACING)) {
            float angle = state.getValue(BaseKitchenBlock.FACING).toYRot();
            transform.rotate(Axis.YP.rotationDegrees(180 - angle));
        }
    }
}
