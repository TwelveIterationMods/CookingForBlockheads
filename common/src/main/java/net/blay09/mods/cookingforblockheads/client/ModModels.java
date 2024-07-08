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
    public static List<DeferredObject<BakedModel>> fridgeDoors;
    public static List<DeferredObject<BakedModel>> fridgeDoorsFlipped;
    public static List<DeferredObject<BakedModel>> fridgeDoorsLargeLower;
    public static List<DeferredObject<BakedModel>> fridgeDoorsLargeUpper;
    public static List<DeferredObject<BakedModel>> fridgeDoorsLargeLowerFlipped;
    public static List<DeferredObject<BakedModel>> fridgeDoorsLargeUpperFlipped;
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
        fridgeDoors = new ArrayList<>(colors.length);
        fridgeDoorsFlipped = new ArrayList<>(colors.length);
        fridgeDoorsLargeLower = new ArrayList<>(colors.length);
        fridgeDoorsLargeUpper = new ArrayList<>(colors.length);
        fridgeDoorsLargeLowerFlipped = new ArrayList<>(colors.length);
        fridgeDoorsLargeUpperFlipped = new ArrayList<>(colors.length);
        for (DyeColor color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            ovenDoors.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door")));
            ovenDoorsActive.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door_active")));
            ovenDoorHandles.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door_handle")));
            fridgeDoors.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_door")));
            fridgeDoorsFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_door_flipped")));
            fridgeDoorsLargeLower.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_lower")));
            fridgeDoorsLargeLowerFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_lower_flipped")));
            fridgeDoorsLargeUpper.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_upper")));
            fridgeDoorsLargeUpperFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_upper_flipped")));
        }

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
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, path);
    }
}
