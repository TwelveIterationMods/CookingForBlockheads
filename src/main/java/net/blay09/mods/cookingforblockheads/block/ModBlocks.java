package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static BlockCookingTable cookingTable;
	public static BlockOven cookingOven;
	public static BlockFridge fridge;
	public static BlockSink sink;
	public static BlockToolRack toolRack;
	public static Block toaster;

	public static void load() {
		cookingTable = new BlockCookingTable();
		registerBlock(cookingTable);
		GameRegistry.registerTileEntity(TileCookingTable.class, cookingTable.getRegistryName().toString());

		cookingOven = new BlockOven();
		registerBlock(cookingOven);
		GameRegistry.registerTileEntity(TileOven.class, cookingOven.getRegistryName().toString());
//
		fridge = new BlockFridge();
		registerBlock(fridge);
		GameRegistry.registerTileEntity(TileFridge.class, fridge.getRegistryName().toString());
//
		sink = new BlockSink();
		registerBlock(sink);
		GameRegistry.registerTileEntity(TileSink.class, sink.getRegistryName().toString());
//
		toolRack = new BlockToolRack();
		registerBlock(toolRack);
		GameRegistry.registerTileEntity(TileToolRack.class, toolRack.getRegistryName().toString());

//		toaster = new BlockToaster();
//		GameRegistry.register(toaster);
//		GameRegistry.registerTileEntity(TileToaster.class, toaster.getRegistryName().toString());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		cookingTable.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		sink.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		toolRack.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		cookingOven.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		fridge.registerModels(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
	}

	public static void registerBlock(Block block) {
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName())); // ...really, Forge? Really?
	}
}
