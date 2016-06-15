package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
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

	public static void load() {
		cookingTable = new BlockCookingTable();
		registerBlock(cookingTable);
		GameRegistry.registerTileEntity(TileCookingTable.class, cookingTable.getRegistryName().toString());

		oven = new BlockOven();
		registerBlock(oven);
		GameRegistry.registerTileEntity(TileOven.class, oven.getRegistryName().toString());

		fridge = new BlockFridge();
		registerBlock(fridge);
		GameRegistry.registerTileEntity(TileFridge.class, fridge.getRegistryName().toString());

		sink = new BlockSink();
		registerBlock(sink);
		GameRegistry.registerTileEntity(TileSink.class, sink.getRegistryName().toString());

		toolRack = new BlockToolRack();
		registerBlock(toolRack);
		GameRegistry.registerTileEntity(TileToolRack.class, toolRack.getRegistryName().toString());

		toaster = new BlockToaster();
		registerBlock(toaster);
		GameRegistry.registerTileEntity(TileToaster.class, toaster.getRegistryName().toString());

		milkJar = new BlockMilkJar();
		registerBlock(milkJar);
		GameRegistry.registerTileEntity(TileMilkJar.class, milkJar.getRegistryName().toString());

		cowJar = new BlockCowJar();
		registerBlock(cowJar);
		GameRegistry.registerTileEntity(TileCowJar.class, cowJar.getRegistryName().toString());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		cookingTable.registerModels(mesher);
		sink.registerModels(mesher);
		toolRack.registerModels(mesher);
		oven.registerModels(mesher);
		fridge.registerModels(mesher);
		toolRack.registerModels(mesher);
		milkJar.registerModels(mesher);
		cowJar.registerModels(mesher);
		toaster.registerModels(mesher);
	}

	public static void registerBlock(Block block) {
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName())); // ...really, Forge? Really?
	}
}
