package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    public static IBakedModel milkJarLiquid;
    public static IBakedModel sinkLiquid;
    public static IBakedModel ovenDoor;
    public static IBakedModel ovenDoorActive;
    public static IBakedModel fridgeDoor;
    public static IBakedModel fridgeDoorFlipped;
    public static IBakedModel fridgeDoorLarge;
    public static IBakedModel fridgeDoorLargeFlipped;
    public static IBakedModel[] counterDoors;
    public static IBakedModel[] counterDoorsFlipped;
    public static IBakedModel[] cabinetDoors;
    public static IBakedModel[] cabinetDoorsFlipped;

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            // Static models used in TileEntityRenderer
            milkJarLiquid = loadAndBakeModel(event, location("block/milk_jar_liquid"));
            sinkLiquid = loadAndBakeModel(event, location("block/sink_liquid"));
            ovenDoor = loadAndBakeModel(event, location("block/oven_door"));
            ovenDoorActive = loadAndBakeModel(event, location("block/oven_door_active"));
            fridgeDoor = loadAndBakeModel(event, location("block/fridge_door"));
            fridgeDoorFlipped = loadAndBakeModel(event, location("block/fridge_door_flipped"));
            fridgeDoorLarge = loadAndBakeModel(event, location("block/fridge_large_door"));
            fridgeDoorLargeFlipped = loadAndBakeModel(event, location("block/fridge_large_door_flipped"));

            DyeColor[] colors = DyeColor.values();
            counterDoors = new IBakedModel[colors.length + 1];
            counterDoors[0] = loadAndBakeModel(event, location("block/counter_door"));
            counterDoorsFlipped = new IBakedModel[colors.length + 1];
            counterDoorsFlipped[0] = loadAndBakeModel(event, location("block/counter_door_flipped"));
            for (DyeColor color : colors) {
                counterDoors[color.getId() + 1] = loadAndBakeModel(event, location("block/counter_door"), replaceTexture(getColoredTerracottaTexture(color)));
                counterDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, location("block/counter_door_flipped"), replaceTexture(getColoredTerracottaTexture(color)));
            }

            cabinetDoors = new IBakedModel[colors.length + 1];
            cabinetDoors[0] = loadAndBakeModel(event, location("block/cabinet_door"));
            cabinetDoorsFlipped = new IBakedModel[colors.length + 1];
            cabinetDoorsFlipped[0] = loadAndBakeModel(event, location("block/cabinet_door_flipped"));
            for (DyeColor color : colors) {
                cabinetDoors[color.getId() + 1] = loadAndBakeModel(event, location("block/cabinet_door"), replaceTexture(getColoredTerracottaTexture(color)));
                cabinetDoorsFlipped[color.getId() + 1] = loadAndBakeModel(event, location("block/cabinet_door_flipped"), replaceTexture(getColoredTerracottaTexture(color)));
            }

            registerColoredKitchenBlock(event, "block/cooking_table", ModBlocks.cookingTable);

            ResourceLocation sinkModel = location("block/sink");
            ResourceLocation sinkFlippedModel = location("block/sink_flipped");
            overrideWithDynamicModel(event, ModBlocks.sink, "block/sink", it -> it.get(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel, it -> {
                if (it.get(SinkBlock.HAS_COLOR)) {
                    return replaceTexture(getColoredTerracottaTexture(it.get(SinkBlock.COLOR)));
                }

                return Collections.emptyMap();
            });

            loadAsDynamicModel(event, ModBlocks.cuttingBoard, "block/cutting_board");

            ResourceLocation toasterModel = location("block/toaster");
            ResourceLocation toasterActiveModel = location("block/toaster_active");
            overrideWithDynamicModel(event, ModBlocks.toaster, "block/toaster", it -> it.get(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel, null);

            loadAsDynamicModel(event, ModBlocks.fruitBasket, "block/fruit_basket");
            loadAsDynamicModel(event, ModBlocks.milkJar, "block/milk_jar");
            loadAsDynamicModel(event, ModBlocks.cowJar, "block/milk_jar");
            ResourceLocation fridgeSmallModel = location("block/fridge");
            ResourceLocation fridgeLargeModel = location("block/fridge_large");
            ResourceLocation fridgeInvisibleModel = location("block/fridge_invisible");
            overrideWithDynamicModel(event, ModBlocks.fridge, "block/fridge", it -> {
                FridgeBlock.FridgeModelType fridgeModelType = it.get(FridgeBlock.MODEL_TYPE);
                switch (fridgeModelType) {
                    case LARGE:
                        return fridgeLargeModel;
                    case INVISIBLE:
                        return fridgeInvisibleModel;
                    default:
                        return fridgeSmallModel;
                }
            }, null);

            registerColoredKitchenBlock(event, "block/counter", ModBlocks.counter);
            registerColoredKitchenBlock(event, "block/corner", ModBlocks.corner);
            registerColoredKitchenBlock(event, "block/cabinet", ModBlocks.cabinet);

            overrideWithDynamicModel(event, ModBlocks.oven, "block/oven", null, state -> {
                String normalTexture = "cookingforblockheads:block/oven_front";
                String activeTexture = "cookingforblockheads:block/oven_front_active";

                boolean isPowered = state.get(OvenBlock.POWERED);
                if (isPowered) {
                    normalTexture = "cookingforblockheads:block/oven_front_powered";
                    activeTexture = "cookingforblockheads:block/oven_front_powered_active";
                }

                boolean isActive = state.get(OvenBlock.ACTIVE);
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

    public static IUnbakedModel retexture(ModelBakery bakery, ResourceLocation baseModel, Map<String, String> replacedTextures) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : replacedTextures.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(baseModel, Collections.emptyList(), replacedTexturesMapped, false, BlockModel.GuiLight.FRONT, ItemCameraTransforms.DEFAULT, Collections.emptyList());

        // We have to call getTextures to initialize the parent field in the model (as that is usually done during stitching, which we're already past)
        blockModel.getTextures(bakery::getUnbakedModel, new HashSet<>());

        return blockModel;
    }

    private static void registerColoredKitchenBlock(ModelBakeEvent event, String modelPath, Block block) {
        overrideWithDynamicModel(event, block, modelPath, null, it -> {
            if (it.get(BlockKitchen.HAS_COLOR)) {
                return replaceTexture(getColoredTerracottaTexture(it.get(BlockKitchen.COLOR)));
            }

            return Collections.emptyMap();
        });
    }

    private static ImmutableMap<String, String> replaceTexture(String texturePath) {
        return ImmutableMap.<String, String>builder().put("texture", texturePath).put("particle", texturePath).build();
    }

    private static String getColoredTerracottaTexture(DyeColor color) {
        return "minecraft:block/" + color.getName().toLowerCase(Locale.ENGLISH) + "_terracotta";
    }

    private static void loadAsDynamicModel(ModelBakeEvent event, Block block, String modelPath) throws Exception {
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
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation) {
        return loadAndBakeModel(event, resourceLocation, Collections.emptyMap());
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation, Map<String, String> textureOverrides) {
        IUnbakedModel model = retexture(event.getModelLoader(), resourceLocation, textureOverrides);
        return model.bakeModel(event.getModelLoader(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, resourceLocation);
    }

    private static void overrideModelIgnoreState(Block block, IBakedModel model, ModelBakeEvent event) {
        block.getStateContainer().getValidStates().forEach((state) -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(state);
            event.getModelRegistry().put(modelLocation, model);
        });
    }
}
