package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            /*IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk_jar_liquid"));
            MilkJarRenderer.modelMilkLiquid = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/sink_liquid"));
            SinkRenderer.modelSinkLiquid = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door"));
            OvenRenderer.modelDoor = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door_active"));
            OvenRenderer.modelDoorActive = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door"));
            FridgeRenderer.modelDoor = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door"));
            FridgeRenderer.modelDoorLarge = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door_handle"));
            FridgeRenderer.modelHandle = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door_handle"));
            FridgeRenderer.modelHandleLarge = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_door_ice_unit"));
            FridgeRenderer.modelDoorIceUnit = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/fridge_large_door_ice_unit"));
            FridgeRenderer.modelDoorIceUnitLarge = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());*/

            IModel cuttingBoard = ModelLoaderRegistry.getModel(new ResourceLocation("cookingforblockheads", "block/cutting_board"));
            CachedDynamicModel cuttingBoardModel = new CachedDynamicModel(event.getModelLoader(), cuttingBoard);
            overrideModelIgnoreState(ModBlocks.cuttingBoard, cuttingBoardModel, event);

            IModel fruitBasket = ModelLoaderRegistry.getModel(new ResourceLocation("cookingforblockheads", "block/fruit_basket"));
            CachedDynamicModel fruitBasketModel = new CachedDynamicModel(event.getModelLoader(), fruitBasket);
            overrideModelIgnoreState(ModBlocks.fruitBasket, fruitBasketModel, event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*EnumDyeColor[] colors = EnumDyeColor.values();
        CounterRenderer.models = new IBakedModel[4][colors.length];
        CounterRenderer.modelsFlipped = new IBakedModel[4][colors.length];
        CabinetRenderer.models = new IBakedModel[4][colors.length];
        CabinetRenderer.modelsFlipped = new IBakedModel[4][colors.length];
        BlockState defaultState = ModBlocks.counter.getDefaultState();
        BlockState state = defaultState.withProperty(CounterBlock.PASS, CounterBlock.ModelPass.DOOR);
        BlockState flippedState = defaultState.withProperty(CounterBlock.PASS, CounterBlock.ModelPass.DOOR_FLIPPED);
        for (int i = 0; i < 4; i++) {
            EnumFacing facing = EnumFacing.getHorizontal(i);
            for (int j = 0; j < colors.length; j++) {
                EnumDyeColor color = colors[j];
                CounterRenderer.models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(CounterBlock.registryName, dummyStateMapper.getPropertyString(state.withProperty(CounterBlock.FACING, facing).withProperty(CounterBlock.COLOR, color).getProperties())));
                CounterRenderer.modelsFlipped[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(CounterBlock.registryName, dummyStateMapper.getPropertyString(flippedState.withProperty(CounterBlock.FACING, facing).withProperty(CounterBlock.COLOR, color).getProperties())));

                CabinetRenderer.models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCabinet.registryName, dummyStateMapper.getPropertyString(state.withProperty(CounterBlock.FACING, facing).withProperty(CounterBlock.COLOR, color).getProperties())));
                CabinetRenderer.modelsFlipped[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCabinet.registryName, dummyStateMapper.getPropertyString(flippedState.withProperty(CounterBlock.FACING, facing).withProperty(CounterBlock.COLOR, color).getProperties())));
            }
        }*/
    }

    private static void overrideModelIgnoreState(Block block, IBakedModel model, ModelBakeEvent event) {
        block.getStateContainer().getValidStates().forEach((state) -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(state);
            event.getModelRegistry().put(modelLocation, model);
        });
    }
}
