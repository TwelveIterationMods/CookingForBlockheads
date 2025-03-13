package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Lists;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.world.item.DyeColor;

import java.util.*;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

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
}
