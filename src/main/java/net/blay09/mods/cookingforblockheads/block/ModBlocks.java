package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static BlockCookingTable cookingTable;
	public static BlockOven oven;
	public static BlockFridge fridge;
	public static BlockSink sink;
	public static BlockToolRack toolRack;
	public static BlockToaster toaster;
	public static BlockMilkJar milkJar;
	public static BlockCowJar cowJar;
	public static BlockSpiceRack spiceRack;
	public static BlockCounter counter;
	public static BlockCorner corner;
	public static BlockKitchenFloor kitchenFloor;

	public static void load() {
		cookingTable = new BlockCookingTable();
		registerBlock(cookingTable);
		GameRegistry.registerTileEntity(TileCookingTable.class, cookingTable.getRegistryNameString());

		oven = new BlockOven();
		registerBlock(oven);
		GameRegistry.registerTileEntity(TileOven.class, oven.getRegistryNameString());

		fridge = new BlockFridge();
		registerBlock(fridge);
		GameRegistry.registerTileEntity(TileFridge.class, fridge.getRegistryNameString());

		sink = new BlockSink();
		registerBlock(sink);
		GameRegistry.registerTileEntity(TileSink.class, sink.getRegistryNameString());

		toolRack = new BlockToolRack();
		registerBlock(toolRack);
		GameRegistry.registerTileEntity(TileToolRack.class, toolRack.getRegistryNameString());

		toaster = new BlockToaster();
		registerBlock(toaster);
		GameRegistry.registerTileEntity(TileToaster.class, toaster.getRegistryNameString());

		milkJar = new BlockMilkJar();
		registerBlock(milkJar);
		GameRegistry.registerTileEntity(TileMilkJar.class, milkJar.getRegistryNameString());

		cowJar = new BlockCowJar();
		registerBlock(cowJar);
		GameRegistry.registerTileEntity(TileCowJar.class, cowJar.getRegistryNameString());

		spiceRack = new BlockSpiceRack();
		registerBlock(spiceRack);
		GameRegistry.registerTileEntity(TileSpiceRack.class, spiceRack.getRegistryNameString());

		counter = new BlockCounter();
		registerBlock(counter);
		GameRegistry.registerTileEntity(TileCounter.class, counter.getRegistryNameString());

		corner = new BlockCorner();
		registerBlock(corner);
		GameRegistry.registerTileEntity(TileCorner.class, corner.getRegistryNameString());

		kitchenFloor = new BlockKitchenFloor();
		registerBlock(kitchenFloor);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		cookingTable.registerModels();
		sink.registerModels();
		toolRack.registerModels();
		oven.registerModels();
		fridge.registerModels();
		toolRack.registerModels();
		milkJar.registerModels();
		cowJar.registerModels();
		toaster.registerModels();
		spiceRack.registerModels();
		counter.registerModels();
		corner.registerModels();
		kitchenFloor.registerModels();
	}

	public static void registerBlock(Block block) {
		GameRegistry.register(block);
		//noinspection ConstantConditions
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
}
