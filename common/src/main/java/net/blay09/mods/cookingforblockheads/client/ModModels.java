package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.*;

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
            final var colorPrefix = color.getSerializedName() + "_";
            counterDoors.add(color.getId() + 1,
                    models.loadModel(id("block/" + colorPrefix + "counter_door")));
            counterDoorsFlipped.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "counter_door_flipped")));
        }

        cabinetDoors = Lists.newArrayListWithCapacity(colors.length + 1);
        cabinetDoors.add(0, models.loadModel(id("block/cabinet_door")));
        cabinetDoorsFlipped = Lists.newArrayListWithCapacity(colors.length + 1);
        cabinetDoorsFlipped.add(0, models.loadModel(id("block/cabinet_door_flipped")));
        for (DyeColor color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            cabinetDoors.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "cabinet_door")));
            cabinetDoorsFlipped.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "cabinet_door_flipped")));
        }

        // ResourceLocation toasterModel = id("block/toaster");
        // ResourceLocation toasterActiveModel = id("block/toaster_active");
        // models.overrideModel(() -> ModBlocks.toaster,
        //         models.loadDynamicModel(id("block/toaster"),
        //                 Set.of(toasterModel, toasterActiveModel),
        //                 it -> it.getValue(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel,
        //                 null,
        //                 ModModels::lowerableFacingTransforms)::get);

       // ResourceLocation fridgeSmallModel = id("block/fridge");
       // ResourceLocation fridgeLargeLowerModel = id("block/fridge_large_lower");
       // ResourceLocation fridgeLargeUpperModel = id("block/fridge_large_upper");
       // models.overrideModel(() -> ModBlocks.fridge,
       //         models.loadDynamicModel(id("block/fridge"), Set.of(fridgeSmallModel, fridgeLargeLowerModel, fridgeLargeUpperModel), it -> {
       //             FridgeBlock.FridgeModelType fridgeModelType = it.getValue(FridgeBlock.MODEL_TYPE);
       //             return switch (fridgeModelType) {
       //                 case LARGE_LOWER -> fridgeLargeLowerModel;
       //                 case LARGE_UPPER -> fridgeLargeUpperModel;
       //                 default -> fridgeSmallModel;
       //             };
       //         }, null, ModModels::lowerableFacingTransforms)::get);

        // models.overrideModel(() -> ModBlocks.cuttingBoard, createLowerableFacingModel("block/cutting_board")::get);
        // models.overrideModel(() -> ModBlocks.fruitBasket, createLowerableFacingModel("block/fruit_basket")::get);
        // models.overrideModel(() -> ModBlocks.milkJar, createLowerableFacingModel("block/milk_jar", List.of(RenderType.cutout()))::get);
        // models.overrideModel(() -> ModBlocks.cowJar, createLowerableFacingModel("block/milk_jar", List.of(RenderType.cutout()))::get);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, path);
    }
}
