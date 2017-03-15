package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CommonProxy;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.SortButtonHunger;
import net.blay09.mods.cookingforblockheads.client.gui.SortButtonName;
import net.blay09.mods.cookingforblockheads.client.gui.SortButtonSaturation;
import net.blay09.mods.cookingforblockheads.client.render.CookingTableRenderer;
import net.blay09.mods.cookingforblockheads.client.render.CounterRenderer;
import net.blay09.mods.cookingforblockheads.client.render.CowJarRenderer;
import net.blay09.mods.cookingforblockheads.client.render.FridgeRenderer;
import net.blay09.mods.cookingforblockheads.client.render.MilkJarRenderer;
import net.blay09.mods.cookingforblockheads.client.render.OvenRenderer;
import net.blay09.mods.cookingforblockheads.client.render.SpiceRackRenderer;
import net.blay09.mods.cookingforblockheads.client.render.ToasterRenderer;
import net.blay09.mods.cookingforblockheads.client.render.ToolRackRenderer;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.blay09.mods.cookingforblockheads.tile.TileCowJar;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.blay09.mods.cookingforblockheads.tile.TileToolRack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

	public static final TextureAtlasSprite[] ovenToolIcons = new TextureAtlasSprite[4];

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		MinecraftForge.EVENT_BUS.register(this);

		ModelLoader.setCustomStateMapper(ModBlocks.fridge, new DefaultStateMapper() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				if(state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.LARGE) {
					return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridge_large", getPropertyString(state.getProperties()));
				} else if (state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.INVISIBLE) {
					return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridge_invisible", getPropertyString(state.getProperties()));
				}
				return super.getModelResourceLocation(state);
			}
		});

		ClientRegistry.bindTileEntitySpecialRenderer(TileToolRack.class, new ToolRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCookingTable.class, new CookingTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new OvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new FridgeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMilkJar.class, new MilkJarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCowJar.class, new CowJarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileToaster.class, new ToasterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSpiceRack.class, new SpiceRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCounter.class, new CounterRenderer());

		ModBlocks.initModels();
		ModItems.initModels();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
				if(world != null && pos != null) {
					TileEntity tileEntity = world.getTileEntity(pos);
					if (tileEntity instanceof TileFridge) {
						return ((TileFridge) tileEntity).getBaseFridge().getFridgeColor().getMapColor().colorValue;
					}
				}
				return 0xFFFFFFFF;
			}
		}, ModBlocks.fridge);

        CookingRegistry.addSortButton(new SortButtonName());
        CookingRegistry.addSortButton(new SortButtonHunger());
        CookingRegistry.addSortButton(new SortButtonSaturation());
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

			model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_door"));
			OvenRenderer.modelDoor = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

			model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door"));
			CounterRenderer.modelDoor = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

			model = ModelLoaderRegistry.getModel(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/counter_door_flipped"));
			CounterRenderer.modelDoorFlipped = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
