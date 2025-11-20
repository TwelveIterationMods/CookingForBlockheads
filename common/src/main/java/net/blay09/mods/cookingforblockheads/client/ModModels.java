package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModModels {
    public static DeferredBlockStateModel milkJarLiquid;
    public static DeferredBlockStateModel cowJarLiquid;
    public static DeferredBlockStateModel sinkLiquid;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> ovenDoors;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> ovenDoorHandles;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> ovenDoorsActive;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoors;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoorsFlipped;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoorsLargeLower;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoorsLargeUpper;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoorsLargeLowerFlipped;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> fridgeDoorsLargeUpperFlipped;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> counterDoors;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> counterDoorsFlipped;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> cabinetDoors;
    public static Map<@Nullable DyeColor, DeferredBlockStateModel> cabinetDoorsFlipped;

    private static Identifier modelId(String name, @Nullable DyeColor color) {
        return id("block/" + (color != null ? color.getSerializedName() + "_" : "") + name);
    }

    public static void initialize(BalmBlockStateModelRegistrar models) {
        milkJarLiquid = models.register(id("block/milk_jar_liquid"));
        cowJarLiquid = models.register(id("block/cow_jar_liquid"));
        sinkLiquid = models.register(id("block/sink_liquid"));

        final var colors = Set.of(DyeColor.values());
        ovenDoors = models.registerDiscriminated(colors, color -> modelId("oven_door", color));
        ovenDoorsActive = models.registerDiscriminated(colors, color -> modelId("oven_door_active", color));
        ovenDoorHandles = models.registerDiscriminated(colors, color -> modelId("oven_door_handle", color));
        fridgeDoors = models.registerDiscriminated(colors, color -> modelId("fridge_door", color));
        fridgeDoorsFlipped = models.registerDiscriminated(colors, color -> modelId("fridge_door_flipped", color));
        fridgeDoorsLargeLower = models.registerDiscriminated(colors, color -> modelId("fridge_large_door_lower", color));
        fridgeDoorsLargeLowerFlipped = models.registerDiscriminated(colors, color -> modelId("fridge_large_door_lower_flipped", color));
        fridgeDoorsLargeUpper = models.registerDiscriminated(colors, color -> modelId("fridge_large_door_upper", color));
        fridgeDoorsLargeUpperFlipped = models.registerDiscriminated(colors, color -> modelId("fridge_large_door_upper_flipped", color));

        final var colorsWithNull = new HashSet<@Nullable DyeColor>(colors);
        colorsWithNull.add(null);
        counterDoors = models.registerDiscriminated(colorsWithNull, color -> modelId("counter_door", color));
        counterDoorsFlipped = models.registerDiscriminated(colorsWithNull, color -> modelId("counter_door_flipped", color));
        cabinetDoors = models.registerDiscriminated(colorsWithNull, color -> modelId("cabinet_door", color));
        cabinetDoorsFlipped = models.registerDiscriminated(colorsWithNull, color -> modelId("cabinet_door_flipped", color));
    }
}
