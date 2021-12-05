package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ModModels {
    public static DeferredObject<BakedModel> milkJarLiquid;
    public static DeferredObject<BakedModel> sinkLiquid;
    public static DeferredObject<BakedModel> ovenDoor;
    public static DeferredObject<BakedModel> ovenDoorHandle;
    public static DeferredObject<BakedModel> ovenDoorActive;
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
        milkJarLiquid = models.loadModel(id("block/milk_jar_liquid"));
        sinkLiquid = models.loadModel(id("block/sink_liquid"));
        ovenDoor = models.loadModel(id("block/oven_door"));
        ovenDoorHandle = models.loadModel(id("block/oven_door_handle"));
        ovenDoorActive = models.loadModel(id("block/oven_door_active"));
        fridgeDoor = models.loadModel(id("block/fridge_door"));
        fridgeDoorFlipped = models.loadModel(id("block/fridge_door_flipped"));
        fridgeDoorLargeLower = models.loadModel(id("block/fridge_large_door_lower"));
        fridgeDoorLargeLowerFlipped = models.loadModel(id("block/fridge_large_door_lower_flipped"));
        fridgeDoorLargeUpper = models.loadModel(id("block/fridge_large_door_upper"));
        fridgeDoorLargeUpperFlipped = models.loadModel(id("block/fridge_large_door_upper_flipped"));

        DyeColor[] colors = DyeColor.values();
        counterDoors = Lists.newArrayListWithCapacity(colors.length + 1);
        counterDoors.add(0, models.loadModel(id("block/counter_door")));
        counterDoorsFlipped = Lists.newArrayListWithCapacity(colors.length + 1);
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
        models.overrideModel(ModBlocks.sink, models.loadDynamicModel(id("block/sink"), it -> it.getValue(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel, it -> {
            if (it.getValue(SinkBlock.HAS_COLOR)) {
                return replaceTexture(getColoredTerracottaTexture(it.getValue(SinkBlock.COLOR)));
            }

            return Collections.emptyMap();
        }, ModModels::lowerableFacingTransforms)::get);

        ResourceLocation toasterModel = id("block/toaster");
        ResourceLocation toasterActiveModel = id("block/toaster_active");
        models.overrideModel(ModBlocks.toaster, models.loadDynamicModel(id("block/toaster"), it -> it.getValue(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel, null, ModModels::lowerableFacingTransforms)::get);

        ResourceLocation fridgeSmallModel = id("block/fridge");
        ResourceLocation fridgeLargeLowerModel = id("block/fridge_large_lower");
        ResourceLocation fridgeLargeUpperModel = id("block/fridge_large_upper");
        models.overrideModel(ModBlocks.fridge, models.loadDynamicModel(id("block/fridge"), it -> {
            FridgeBlock.FridgeModelType fridgeModelType = it.getValue(FridgeBlock.MODEL_TYPE);
            return switch (fridgeModelType) {
                case LARGE_LOWER -> fridgeLargeLowerModel;
                case LARGE_UPPER -> fridgeLargeUpperModel;
                default -> fridgeSmallModel;
            };
        }, null, ModModels::lowerableFacingTransforms)::get);

        models.overrideModel(ModBlocks.oven, models.loadDynamicModel(id("block/oven"), null, state -> {
            String normalTexture = "cookingforblockheads:block/oven_front";
            String activeTexture = "cookingforblockheads:block/oven_front_active";

            boolean isPowered = state.getValue(OvenBlock.POWERED);
            if (isPowered) {
                normalTexture = "cookingforblockheads:block/oven_front_powered";
                activeTexture = "cookingforblockheads:block/oven_front_powered_active";
            }

            boolean isActive = state.getValue(OvenBlock.ACTIVE);
            if (isActive || isPowered) {
                return ImmutableMap.of("ovenfront", isActive ? activeTexture : normalTexture);
            }

            return Collections.emptyMap();
        }, ModModels::lowerableFacingTransforms)::get);

        models.overrideModel(ModBlocks.cuttingBoard, createLowerableFacingModel("block/cutting_board")::get);
        models.overrideModel(ModBlocks.fruitBasket, createLowerableFacingModel("block/fruit_basket")::get);
        models.overrideModel(ModBlocks.milkJar, createLowerableFacingModel("block/milk_jar")::get);
        models.overrideModel(ModBlocks.cowJar, createLowerableFacingModel("block/milk_jar")::get);

        registerColoredKitchenBlock(BalmClient.getModels(), ModBlocks.cookingTable, "block/cooking_table");
        registerColoredKitchenBlock(BalmClient.getModels(), ModBlocks.counter, "block/counter");
        registerColoredKitchenBlock(BalmClient.getModels(), ModBlocks.corner, "block/corner");
        registerColoredKitchenBlock(BalmClient.getModels(), ModBlocks.hangingCorner, "block/hanging_corner");
        registerColoredKitchenBlock(BalmClient.getModels(), ModBlocks.cabinet, "block/cabinet");
    }

    private static DeferredObject<BakedModel> createLowerableFacingModel(String modelPath) {
        return BalmClient.getModels().loadDynamicModel(id(modelPath), null, null, ModModels::lowerableFacingTransforms);
    }

    private static void registerColoredKitchenBlock(BalmModels models, Block block, String modelPath) {
        models.overrideModel(block, models.loadDynamicModel(id(modelPath), null, it -> {
            if (it.getValue(BlockKitchen.HAS_COLOR)) {
                return replaceTexture(getColoredTerracottaTexture(it.getValue(BlockKitchen.COLOR)));
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
        if (state.hasProperty(BlockKitchen.LOWERED) && state.getValue(BlockKitchen.LOWERED)) {
            transform.translate(new Vector3f(0, -0.05f, 0f));
        }

        if (state.hasProperty(BlockKitchen.FACING)) {
            float angle = state.getValue(BlockKitchen.FACING).toYRot();
            transform.multiply(new Quaternion(0f, 180 - angle, 0f, true));
        }
    }
}
