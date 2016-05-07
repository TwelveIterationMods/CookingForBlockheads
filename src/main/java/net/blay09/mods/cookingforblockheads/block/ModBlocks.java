package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static BlockCookingTable cookingTable;
	public static BlockOven oven;
	public static BlockFridge fridge;
	public static BlockSink sink;
	public static BlockToolRack toolRack;

	public static void load() {
		cookingTable = new BlockCookingTable();
		GameRegistry.registerBlock(cookingTable);
		GameRegistry.registerTileEntity(TileCookingTable.class, cookingTable.getRegistryName());

		oven = new BlockOven();
		GameRegistry.registerBlock(oven);
		GameRegistry.registerTileEntity(TileOven.class, oven.getRegistryName());
//
		fridge = new BlockFridge();
		GameRegistry.registerBlock(fridge);
		GameRegistry.registerTileEntity(TileFridge.class, fridge.getRegistryName());
//
		sink = new BlockSink();
		GameRegistry.registerBlock(sink);
		GameRegistry.registerTileEntity(TileSink.class, sink.getRegistryName());
//
		toolRack = new BlockToolRack();
		GameRegistry.registerBlock(toolRack);
		GameRegistry.registerTileEntity(TileToolRack.class, toolRack.getRegistryName());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		cookingTable.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		sink.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		toolRack.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		oven.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		fridge.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
	}

}
