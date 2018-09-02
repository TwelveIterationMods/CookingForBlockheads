package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CommonProxy;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockCabinet;
import net.blay09.mods.cookingforblockheads.block.BlockCounter;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

    public static final TextureAtlasSprite[] ovenToolIcons = new TextureAtlasSprite[4];

    private final DefaultStateMapper dummyStateMapper = new DefaultStateMapper();

    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomStateMapper(ModBlocks.fridge, new DefaultStateMapper() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                if (state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.LARGE) {
                    return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridge_large", getPropertyString(state.getProperties()));
                } else if (state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.INVISIBLE) {
                    return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridge_invisible", getPropertyString(state.getProperties()));
                }
                return super.getModelResourceLocation(state);
            }
        });

        DefaultStateMapper ignorePropertiesStateMapper = new DefaultStateMapper() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                ResourceLocation location = state.getBlock().getRegistryName();
                return new ModelResourceLocation(location != null ? location.toString() : "", "normal");
            }
        };
        ModelLoader.setCustomStateMapper(ModBlocks.cuttingBoard, ignorePropertiesStateMapper);
        ModelLoader.setCustomStateMapper(ModBlocks.fruitBasket, ignorePropertiesStateMapper);

        ClientRegistry.bindTileEntitySpecialRenderer(TileToolRack.class, new ToolRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCookingTable.class, new CookingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new OvenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new FridgeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMilkJar.class, new MilkJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCowJar.class, new CowJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileToaster.class, new ToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSpiceRack.class, new SpiceRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new CounterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCabinet.class, new CabinetRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSink.class, new SinkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFruitBasket.class, new FruitBasketRenderer());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof TileFridge) {
                    return ((TileFridge) tileEntity).getBaseFridge().getFridgeColor().getColorValue();
                }
            }
            return 0xFFFFFFFF;
        }, ModBlocks.fridge);
    }

    @SubscribeEvent
    public void registerIcons(TextureStitchEvent.Pre event) {
        ovenToolIcons[0] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slot_bakeware"));
        ovenToolIcons[1] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slot_pot"));
        ovenToolIcons[2] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slot_saucepan"));
        ovenToolIcons[3] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slot_skillet"));
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        try {
            IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk_jar_liquid"));
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
            FridgeRenderer.modelDoorIceUnitLarge = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            IModel cuttingBoard = ModelLoaderRegistry.getModel(new ResourceLocation("cookingforblockheads", "block/cutting_board"));
            event.getModelRegistry().putObject(new ModelResourceLocation("cookingforblockheads:cutting_board", "normal"), new LowerableFacingOnDemandModel(cuttingBoard));

            IModel fruitBasket = ModelLoaderRegistry.getModel(new ResourceLocation("cookingforblockheads", "block/fruit_basket"));
            event.getModelRegistry().putObject(new ModelResourceLocation("cookingforblockheads:fruit_basket", "normal"), new LowerableFacingOnDemandModel(fruitBasket));
        } catch (Exception e) {
            e.printStackTrace();
        }

        EnumDyeColor[] colors = EnumDyeColor.values();
        CounterRenderer.models = new IBakedModel[4][colors.length];
        CounterRenderer.modelsFlipped = new IBakedModel[4][colors.length];
        CabinetRenderer.models = new IBakedModel[4][colors.length];
        CabinetRenderer.modelsFlipped = new IBakedModel[4][colors.length];
        IBlockState defaultState = ModBlocks.counter.getDefaultState();
        IBlockState state = defaultState.withProperty(BlockCounter.PASS, BlockCounter.ModelPass.DOOR);
        IBlockState flippedState = defaultState.withProperty(BlockCounter.PASS, BlockCounter.ModelPass.DOOR_FLIPPED);
        for (int i = 0; i < 4; i++) {
            EnumFacing facing = EnumFacing.getHorizontal(i);
            for (int j = 0; j < colors.length; j++) {
                EnumDyeColor color = colors[j];
                CounterRenderer.models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCounter.registryName, dummyStateMapper.getPropertyString(state.withProperty(BlockCounter.FACING, facing).withProperty(BlockCounter.COLOR, color).getProperties())));
                CounterRenderer.modelsFlipped[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCounter.registryName, dummyStateMapper.getPropertyString(flippedState.withProperty(BlockCounter.FACING, facing).withProperty(BlockCounter.COLOR, color).getProperties())));

                CabinetRenderer.models[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCabinet.registryName, dummyStateMapper.getPropertyString(state.withProperty(BlockCounter.FACING, facing).withProperty(BlockCounter.COLOR, color).getProperties())));
                CabinetRenderer.modelsFlipped[i][j] = event.getModelRegistry().getObject(new ModelResourceLocation(BlockCabinet.registryName, dummyStateMapper.getPropertyString(flippedState.withProperty(BlockCounter.FACING, facing).withProperty(BlockCounter.COLOR, color).getProperties())));
            }
        }
    }

}
