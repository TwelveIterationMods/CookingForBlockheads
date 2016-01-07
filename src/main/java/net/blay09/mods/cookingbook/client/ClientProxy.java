package net.blay09.mods.cookingbook.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.blay09.mods.cookingbook.CommonProxy;
import net.blay09.mods.cookingbook.CookingForBlockheads;
import net.blay09.mods.cookingbook.block.*;
import net.blay09.mods.cookingbook.client.render.*;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	public static final IIcon[] ovenToolIcons = new IIcon[4];

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFridge.class, new TileEntityFridgeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToolRack.class, new TileEntityToolRackRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingOven.class, new TileEntityOvenRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySink.class, new TileEntitySinkRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingTable.class, new TileEntityCookingTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToaster.class, new TileEntityToasterRenderer());
		RenderingRegistry.registerBlockHandler(FridgeBlockRenderer.RENDER_ID, new FridgeBlockRenderer());
		RenderingRegistry.registerBlockHandler(ToolRackBlockRenderer.RENDER_ID, new ToolRackBlockRenderer());
		RenderingRegistry.registerBlockHandler(OvenBlockRenderer.RENDER_ID, new OvenBlockRenderer());
		RenderingRegistry.registerBlockHandler(SinkBlockRenderer.RENDER_ID, new SinkBlockRenderer());
		RenderingRegistry.registerBlockHandler(CookingTableBlockRenderer.RENDER_ID, new CookingTableBlockRenderer());
		RenderingRegistry.registerBlockHandler(ToasterBlockRenderer.RENDER_ID, new ToasterBlockRenderer());
	}

	@SubscribeEvent
	public void keyInput(InputEvent.KeyInputEvent event) {}

	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {
		if(event.map.getTextureType() == 1) {
			ovenToolIcons[0] = event.map.registerIcon(CookingForBlockheads.MOD_ID + ":bakeware");
			ovenToolIcons[1] = event.map.registerIcon(CookingForBlockheads.MOD_ID + ":pot");
			ovenToolIcons[2] = event.map.registerIcon(CookingForBlockheads.MOD_ID + ":saucepan");
			ovenToolIcons[3] = event.map.registerIcon(CookingForBlockheads.MOD_ID + ":skillet");
		}
	}
}
