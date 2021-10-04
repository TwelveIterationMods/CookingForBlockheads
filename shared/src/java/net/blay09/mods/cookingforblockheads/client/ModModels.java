package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ModModels {
    public static BakedModel milkJarLiquid;
    public static BakedModel sinkLiquid;
    public static BakedModel ovenDoor;
    public static BakedModel ovenDoorHandle;
    public static BakedModel ovenDoorActive;
    public static BakedModel fridgeDoor;
    public static BakedModel fridgeDoorFlipped;
    public static BakedModel fridgeDoorLargeLower;
    public static BakedModel fridgeDoorLargeUpper;
    public static BakedModel fridgeDoorLargeLowerFlipped;
    public static BakedModel fridgeDoorLargeUpperFlipped;
    public static BakedModel[] counterDoors;
    public static BakedModel[] counterDoorsFlipped;
    public static BakedModel[] cabinetDoors;
    public static BakedModel[] cabinetDoorsFlipped;

    /*public static void onModelBake(ModelBakeEvent event) { TODO port
        try {
            // Static models used in TileEntityRenderer
            milkJarLiquid = loadAndBakeModel(event, location("block/milk_jar_liquid"));
            sinkLiquid = loadAndBakeModel(event, location("block/sink_liquid"));
            ovenDoor = loadAndBakeModel(event, location("block/oven_door"));
            ovenDoorHandle = loadAndBakeModel(event, location("block/oven_door_handle"));
            ovenDoorActive = loadAndBakeModel(event, location("block/oven_door_active"));
            fridgeDoor = loadAndBakeModel(event, location("block/fridge_door"));
            fridgeDoorFlipped = loadAndBakeModel(event, location("block/fridge_door_flipped"));
            fridgeDoorLargeLower = loadAndBakeModel(event, location("block/fridge_large_door_lower"));
            fridgeDoorLargeLowerFlipped = loadAndBakeModel(event, location("block/fridge_large_door_lower_flipped"));
            fridgeDoorLargeUpper = loadAndBakeModel(event, location("block/fridge_large_door_upper"));
            fridgeDoorLargeUpperFlipped = loadAndBakeModel(event, location("block/fridge_large_door_upper_flipped"));

            DyeColor[] colors = DyeColor.values();
            counterDoors = new BakedModel[colors.length + 1];
            counterDoors[0] = loadAndBakeModel(event, location("block/counter_door"));
            counterDoorsFlipped = new BakedModel[colors.length + 1];
            counterDoorsFlipped[0] = loadAndBakeModel(event, location("block/counter_door_flipped"));
            for (DyeColor color : colors) {
                counterDoors[color.getId() + 1] = loadAndBakeModel(event, location("block/counter_door"), replaceTexture(getColoredTerracottaTexture(color)));
                counterDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, location("block/counter_door_flipped"), replaceTexture(getColoredTerracottaTexture(color)));
            }

            cabinetDoors = new BakedModel[colors.length + 1];
            cabinetDoors[0] = loadAndBakeModel(event, location("block/cabinet_door"));
            cabinetDoorsFlipped = new BakedModel[colors.length + 1];
            cabinetDoorsFlipped[0] = loadAndBakeModel(event, location("block/cabinet_door_flipped"));
            for (DyeColor color : colors) {
                cabinetDoors[color.getId() + 1] = loadAndBakeModel(event, location("block/cabinet_door"), replaceTexture(getColoredTerracottaTexture(color)));
                cabinetDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, location("block/cabinet_door_flipped"), replaceTexture(getColoredTerracottaTexture(color)));
            }

            registerColoredKitchenBlock(event, "block/cooking_table", ModBlocks.cookingTable);

            ResourceLocation sinkModel = location("block/sink");
            ResourceLocation sinkFlippedModel = location("block/sink_flipped");
            overrideWithDynamicModel(event, ModBlocks.sink, "block/sink", it -> it.getValue(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel, it -> {
                if (it.getValue(SinkBlock.HAS_COLOR)) {
                    return replaceTexture(getColoredTerracottaTexture(it.getValue(SinkBlock.COLOR)));
                }

                return Collections.emptyMap();
            });

            loadAsDynamicModel(event, ModBlocks.cuttingBoard, "block/cutting_board");

            ResourceLocation toasterModel = location("block/toaster");
            ResourceLocation toasterActiveModel = location("block/toaster_active");
            overrideWithDynamicModel(event, ModBlocks.toaster, "block/toaster", it -> it.getValue(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel, null);

            loadAsDynamicModel(event, ModBlocks.fruitBasket, "block/fruit_basket");
            loadAsDynamicModel(event, ModBlocks.milkJar, "block/milk_jar");
            loadAsDynamicModel(event, ModBlocks.cowJar, "block/milk_jar");
            ResourceLocation fridgeSmallModel = location("block/fridge");
            ResourceLocation fridgeLargeLowerModel = location("block/fridge_large_lower");
            ResourceLocation fridgeLargeUpperModel = location("block/fridge_large_upper");
            overrideWithDynamicModel(event, ModBlocks.fridge, "block/fridge", it -> {
                FridgeBlock.FridgeModelType fridgeModelType = it.getValue(FridgeBlock.MODEL_TYPE);
                return switch (fridgeModelType) {
                    case LARGE_LOWER -> fridgeLargeLowerModel;
                    case LARGE_UPPER -> fridgeLargeUpperModel;
                    default -> fridgeSmallModel;
                };
            }, null);

            registerColoredKitchenBlock(event, "block/counter", ModBlocks.counter);
            registerColoredKitchenBlock(event, "block/corner", ModBlocks.corner);
            registerColoredKitchenBlock(event, "block/hanging_corner", ModBlocks.hangingCorner);
            registerColoredKitchenBlock(event, "block/cabinet", ModBlocks.cabinet);

            overrideWithDynamicModel(event, ModBlocks.oven, "block/oven", null, state -> {
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ResourceLocation location(String path) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, path);
    }

    public static UnbakedModel retexture(ModelBakery bakery, ResourceLocation baseModel, Map<String, String> replacedTextures) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : replacedTextures.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(baseModel, Collections.emptyList(), replacedTexturesMapped, false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());

        // We have to call getMaterials to initialize the parent field in the model (as that is usually done during stitching, which we're already past)
        blockModel.getMaterials(bakery::getModel, new HashSet<>());

        return blockModel;
    }

    private static void registerColoredKitchenBlock(ModelBakeEvent event, String modelPath, Block block) {
        overrideWithDynamicModel(event, block, modelPath, null, it -> {
            if (it.getValue(BlockKitchen.HAS_COLOR)) {
                return replaceTexture(getColoredTerracottaTexture(it.getValue(BlockKitchen.COLOR)));
            }

            return Collections.emptyMap();
        });
    }

    private static ImmutableMap<String, String> replaceTexture(String texturePath) {
        return ImmutableMap.<String, String>builder().put("texture", texturePath).put("particle", texturePath).build();
    }

    private static String getColoredTerracottaTexture(DyeColor color) {
        return "minecraft:block/" + color.name().toLowerCase(Locale.ENGLISH) + "_terracotta";
    }

    private static void loadAsDynamicModel(ModelBakeEvent event, Block block, String modelPath) {
        overrideWithDynamicModel(event, block, modelPath, null, null);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction) {
        ResourceLocation modelLocation = location(modelPath);
        if (modelFunction == null) {
            modelFunction = it -> modelLocation;
        }

        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), modelFunction, null, textureMapFunction, modelLocation);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    @Nullable
    private static BakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation) {
        return loadAndBakeModel(event, resourceLocation, Collections.emptyMap());
    }

    @Nullable
    private static BakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation, Map<String, String> textureOverrides) {
        ModelBakery bakery = event.getModelLoader();
        UnbakedModel model = retexture(bakery, resourceLocation, textureOverrides);
        return model.bake(bakery, ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, resourceLocation);
    }

    private static void overrideModelIgnoreState(Block block, BakedModel model, ModelBakeEvent event) {
        block.getStateDefinition().getPossibleStates().forEach((state) -> {
            ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
            event.getModelRegistry().put(modelLocation, model);
        });
    }*/
}
