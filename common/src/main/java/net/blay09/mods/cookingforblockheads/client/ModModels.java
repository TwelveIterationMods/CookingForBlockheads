package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.item.DyeColor;

import java.util.*;

public class ModModels {
    public static DeferredObject<BlockStateModel> milkJarLiquid;
    public static DeferredObject<BlockStateModel> cowJarLiquid;
    public static DeferredObject<BlockStateModel> sinkLiquid;
    public static List<DeferredObject<BlockStateModel>> ovenDoors;
    public static List<DeferredObject<BlockStateModel>> ovenDoorHandles;
    public static List<DeferredObject<BlockStateModel>> ovenDoorsActive;
    public static List<DeferredObject<BlockStateModel>> fridgeDoors;
    public static List<DeferredObject<BlockStateModel>> fridgeDoorsFlipped;
    public static List<DeferredObject<BlockStateModel>> fridgeDoorsLargeLower;
    public static List<DeferredObject<BlockStateModel>> fridgeDoorsLargeUpper;
    public static List<DeferredObject<BlockStateModel>> fridgeDoorsLargeLowerFlipped;
    public static List<DeferredObject<BlockStateModel>> fridgeDoorsLargeUpperFlipped;
    public static List<DeferredObject<BlockStateModel>> counterDoors;
    public static List<DeferredObject<BlockStateModel>> counterDoorsFlipped;
    public static List<DeferredObject<BlockStateModel>> cabinetDoors;
    public static List<DeferredObject<BlockStateModel>> cabinetDoorsFlipped;

    public static void initialize(BalmModels models) {
        DyeColor[] colors = DyeColor.values();

        // TODO milkJarLiquid = models.loadModel(id("block/milk_jar_liquid"));
        // TODO cowJarLiquid = models.loadModel(id("block/cow_jar_liquid"));
        // TODO sinkLiquid = models.loadModel(id("block/sink_liquid"));
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
            // TODO ovenDoors.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door")));
            // TODO ovenDoorsActive.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door_active")));
            // TODO ovenDoorHandles.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "oven_door_handle")));
            // TODO fridgeDoors.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_door")));
            // TODO fridgeDoorsFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_door_flipped")));
            // TODO fridgeDoorsLargeLower.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_lower")));
            // TODO fridgeDoorsLargeLowerFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_lower_flipped")));
            // TODO fridgeDoorsLargeUpper.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_upper")));
            // TODO fridgeDoorsLargeUpperFlipped.add(color.getId(), models.loadModel(id("block/" + colorPrefix + "fridge_large_door_upper_flipped")));
        }

        counterDoors = new ArrayList<>(colors.length + 1);
        // TODO counterDoors.add(0, models.loadModel(id("block/counter_door")));
        counterDoorsFlipped = new ArrayList<>(colors.length + 1);
        // TODO counterDoorsFlipped.add(0, models.loadModel(id("block/counter_door_flipped")));
        for (DyeColor color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            // TODO counterDoors.add(color.getId() + 1,
            // TODO         models.loadModel(id("block/" + colorPrefix + "counter_door")));
            // TODO counterDoorsFlipped.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "counter_door_flipped")));
        }

        cabinetDoors = Lists.newArrayListWithCapacity(colors.length + 1);
        // TODO cabinetDoors.add(0, models.loadModel(id("block/cabinet_door")));
        // TODO cabinetDoorsFlipped = Lists.newArrayListWithCapacity(colors.length + 1);
        // TODO cabinetDoorsFlipped.add(0, models.loadModel(id("block/cabinet_door_flipped")));
        for (DyeColor color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            // TODO cabinetDoors.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "cabinet_door")));
            // TODO cabinetDoorsFlipped.add(color.getId() + 1, models.loadModel(id("block/" + colorPrefix + "cabinet_door_flipped")));
        }
    }
}
