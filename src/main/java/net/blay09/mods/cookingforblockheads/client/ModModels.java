package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    public static IBakedModel milkJarLiquid;
    public static IBakedModel sinkLiquid;
    public static IBakedModel ovenDoor;
    public static IBakedModel ovenDoorActive;
    public static IBakedModel fridgeDoor;
    public static IBakedModel fridgeDoorLarge;
    public static IBakedModel[] counterDoors;
    public static IBakedModel[] counterDoorsFlipped;
    public static IBakedModel[] cabinetDoors;
    public static IBakedModel[] cabinetDoorsFlipped;

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            milkJarLiquid = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk_jar_liquid"));
            sinkLiquid = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink_liquid"));
            ovenDoor = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door"));
            ovenDoorActive = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door_active"));
            fridgeDoor = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door"));
            fridgeDoorLarge = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door"));

            DyeColor[] colors = DyeColor.values();
            counterDoors = new IBakedModel[colors.length];
            counterDoorsFlipped = new IBakedModel[colors.length];
            for (DyeColor color : colors) {
                counterDoors[color.getId()] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door"));
                counterDoorsFlipped[color.getId()] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door_flipped"));
            }

            cabinetDoors = new IBakedModel[colors.length];
            cabinetDoorsFlipped = new IBakedModel[colors.length];
            for (DyeColor color : colors) {
                cabinetDoors[color.getId()] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door"));
                cabinetDoorsFlipped[color.getId()] = loadAndBakeModel(event, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/cabinet_door_flipped"));
            }

            overrideWithDynamicModel(event, ModBlocks.cookingTable, "block/cooking_table");

            IModel sinkModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink"));
            IModel sinkFlippedModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink_flipped"));
            overrideWithDynamicModel(event, ModBlocks.sink, it -> it.get(SinkBlock.FLIPPED) ? sinkFlippedModel : sinkModel);

            overrideWithDynamicModel(event, ModBlocks.cuttingBoard, "block/cutting_board");

            IModel toasterModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/toaster"));
            IModel toasterActiveModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/toaster_active"));
            overrideWithDynamicModel(event, ModBlocks.toaster, it -> it.get(ToasterBlock.ACTIVE) ? toasterActiveModel : toasterModel);

            overrideWithDynamicModel(event, ModBlocks.fruitBasket, "block/fruit_basket");
            overrideWithDynamicModel(event, ModBlocks.milkJar, "block/milk_jar");
            overrideWithDynamicModel(event, ModBlocks.cowJar, "block/milk_jar");
            IModel fridgeSmallModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge"));
            IModel fridgeLargeModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large"));
            IModel fridgeInvisibleModel = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_invisible"));
            overrideWithDynamicModel(event, ModBlocks.fridge, it -> {
                FridgeBlock.FridgeModelType fridgeModelType = it.get(FridgeBlock.MODEL_TYPE);
                switch (fridgeModelType) {
                    case LARGE:
                        return fridgeLargeModel;
                    case INVISIBLE:
                        return fridgeInvisibleModel;
                    default:
                        return fridgeSmallModel;
                }
            });
            overrideWithDynamicModel(event, ModBlocks.counter, "block/counter");
            overrideWithDynamicModel(event, ModBlocks.corner, "block/corner");
            overrideWithDynamicModel(event, ModBlocks.cabinet, "block/cabinet");
            overrideWithDynamicModel(event, ModBlocks.oven, "block/oven", null, state -> {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
                String normalTexture = "cookingforblockheads:block/oven_front";
                String activeTexture = "cookingforblockheads:block/oven_front_active";
                if (state.get(OvenBlock.POWERED)) {
                    normalTexture = "cookingforblockheads:block/oven_front_powered";
                    activeTexture = "cookingforblockheads:block/oven_front_powered_active";
                }

                builder.put("ovenfront", state.get(OvenBlock.ACTIVE) ? activeTexture : normalTexture);
                return builder.build();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath) throws Exception {
        overrideWithDynamicModel(event, block, modelPath, null, null);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, String modelPath, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, ImmutableMap<String, String>> textureMapFunction) throws Exception {
        IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, modelPath));
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), it -> model, parts, textureMapFunction);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, Function<BlockState, IModel> modelFunction) throws Exception {
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), modelFunction, null, null);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    private static void overrideWithDynamicModel(ModelBakeEvent event, Block block, Function<BlockState, IModel> modelFunction, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, ImmutableMap<String, String>> textureMapFunction) throws Exception {
        CachedDynamicModel dynamicModel = new CachedDynamicModel(event.getModelLoader(), modelFunction, parts, textureMapFunction);
        overrideModelIgnoreState(block, dynamicModel, event);
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation) throws Exception {
        IModel model = ModelLoaderRegistry.getModel(resourceLocation);
        BasicState modelState = new BasicState(model.getDefaultState(), false);
        return model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), modelState, DefaultVertexFormats.BLOCK);
    }

    private static void overrideModelIgnoreState(Block block, IBakedModel model, ModelBakeEvent event) {
        block.getStateContainer().getValidStates().forEach((state) -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(state);
            event.getModelRegistry().put(modelLocation, model);
        });
    }
}
