package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.cookingforblockheads.CommonProxy;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.render.CookingTableRenderer;
import net.blay09.mods.cookingforblockheads.client.render.FridgeRenderer;
import net.blay09.mods.cookingforblockheads.client.render.OvenRenderer;
import net.blay09.mods.cookingforblockheads.client.render.ToolRackRenderer;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.blay09.mods.cookingforblockheads.tile.TileToolRack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	public static TextureAtlasSprite[] ovenToolIcons = new TextureAtlasSprite[4];

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		MinecraftForge.EVENT_BUS.register(this);

		ModelLoader.setCustomStateMapper(ModBlocks.fridge, new DefaultStateMapper() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				if(state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.LARGE) {
					return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridgeLarge", getPropertyString(state.getProperties()));
				} else if (state.getValue(BlockFridge.TYPE) == BlockFridge.FridgeType.INVISIBLE) {
					return new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":fridgeInvisible", getPropertyString(state.getProperties()));
				}
				return super.getModelResourceLocation(state);
			}
		});

		ClientRegistry.bindTileEntitySpecialRenderer(TileToolRack.class, new ToolRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCookingTable.class, new CookingTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileOven.class, new OvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new FridgeRenderer());
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		ModBlocks.initModels();
		ModItems.initModels();
	}

	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {
		ovenToolIcons[0] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slotBakeware"));
		ovenToolIcons[1] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slotPot"));
		ovenToolIcons[2] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slotSaucepan"));
		ovenToolIcons[3] = event.getMap().registerSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "items/slotSkillet"));
	}

	@Override
	public IAnimationStateMachine loadAnimationStateMachine(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
		return ModelLoaderRegistry.loadASM(location, parameters);
	}

	@Override
	public void addScheduledTask(Runnable runnable) {
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}
}
