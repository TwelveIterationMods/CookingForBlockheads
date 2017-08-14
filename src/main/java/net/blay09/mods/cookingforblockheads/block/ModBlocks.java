package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.tile.TileCookingTable;
import net.blay09.mods.cookingforblockheads.tile.TileCorner;
import net.blay09.mods.cookingforblockheads.tile.TileCounter;
import net.blay09.mods.cookingforblockheads.tile.TileCowJar;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.blay09.mods.cookingforblockheads.tile.TileMilkJar;
import net.blay09.mods.cookingforblockheads.tile.TileOven;
import net.blay09.mods.cookingforblockheads.tile.TileSink;
import net.blay09.mods.cookingforblockheads.tile.TileSpiceRack;
import net.blay09.mods.cookingforblockheads.tile.TileToaster;
import net.blay09.mods.cookingforblockheads.tile.TileToolRack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder(CookingForBlockheads.MOD_ID)
public class ModBlocks {

	@GameRegistry.ObjectHolder(BlockCookingTable.name)
	public static final Block cookingTable = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockOven.name)
	public static final Block oven = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockFridge.name)
	public static final Block fridge = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockSink.name)
	public static final Block sink = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockToolRack.name)
	public static final Block toolRack = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockToaster.name)
	public static final Block toaster = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockMilkJar.name)
	public static final Block milkJar = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockCowJar.name)
	public static final Block cowJar = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockSpiceRack.name)
	public static final Block spiceRack = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockCounter.name)
	public static final Block counter = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockCorner.name)
	public static final Block corner = Blocks.AIR;

	@GameRegistry.ObjectHolder(BlockKitchenFloor.name)
	public static final Block kitchenFloor = Blocks.AIR;

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				new BlockCookingTable().setRegistryName(BlockCookingTable.name),
				new BlockOven().setRegistryName(BlockOven.name),
				new BlockFridge().setRegistryName(BlockFridge.name),
				new BlockSink().setRegistryName(BlockSink.name),
				new BlockToolRack().setRegistryName(BlockToolRack.name),
				new BlockToaster().setRegistryName(BlockToaster.name),
				new BlockMilkJar().setRegistryName(BlockMilkJar.name),
				new BlockCowJar().setRegistryName(BlockCowJar.name),
				new BlockSpiceRack().setRegistryName(BlockSpiceRack.name),
				new BlockCounter().setRegistryName(BlockCounter.name),
				new BlockCorner().setRegistryName(BlockCorner.name),
				new BlockKitchenFloor().setRegistryName(BlockKitchenFloor.name)
		);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				new ItemBlock(ModBlocks.cookingTable).setRegistryName(BlockCookingTable.name),
				new ItemBlock(ModBlocks.oven).setRegistryName(BlockOven.name),
				new ItemBlock(ModBlocks.fridge).setRegistryName(BlockFridge.name),
				new ItemBlock(ModBlocks.sink).setRegistryName(BlockSink.name),
				new ItemBlock(ModBlocks.toolRack).setRegistryName(BlockToolRack.name),
				new ItemBlock(ModBlocks.toaster).setRegistryName(BlockToaster.name),
				new ItemBlock(ModBlocks.milkJar).setRegistryName(BlockMilkJar.name),
				new ItemBlock(ModBlocks.cowJar).setRegistryName(BlockCowJar.name),
				new ItemBlock(ModBlocks.spiceRack).setRegistryName(BlockSpiceRack.name),
				new ItemBlock(ModBlocks.counter).setRegistryName(BlockCounter.name),
				new ItemBlock(ModBlocks.corner).setRegistryName(BlockCorner.name),
				new ItemBlock(ModBlocks.kitchenFloor).setRegistryName(BlockKitchenFloor.name)
		);
	}

	public static void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(cookingTable), 0, new ModelResourceLocation(BlockCookingTable.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(oven), 0, new ModelResourceLocation(BlockOven.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(fridge), 0, new ModelResourceLocation(BlockFridge.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sink), 0, new ModelResourceLocation(BlockSink.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(toolRack), 0, new ModelResourceLocation(BlockToolRack.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(toaster), 0, new ModelResourceLocation(BlockToaster.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(milkJar), 0, new ModelResourceLocation(BlockMilkJar.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(cowJar), 0, new ModelResourceLocation(BlockCowJar.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(spiceRack), 0, new ModelResourceLocation(BlockSpiceRack.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(counter), 0, new ModelResourceLocation(BlockCounter.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(corner), 0, new ModelResourceLocation(BlockCorner.registryName, "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(kitchenFloor), 0, new ModelResourceLocation(BlockKitchenFloor.registryName, "inventory"));
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileCookingTable.class, BlockCookingTable.registryName.toString());
		GameRegistry.registerTileEntity(TileOven.class, BlockOven.registryName.toString());
		GameRegistry.registerTileEntity(TileFridge.class, BlockFridge.registryName.toString());
		GameRegistry.registerTileEntity(TileSink.class, BlockSink.registryName.toString());
		GameRegistry.registerTileEntity(TileToolRack.class, BlockToolRack.registryName.toString());
		GameRegistry.registerTileEntity(TileToaster.class, BlockToaster.registryName.toString());
		GameRegistry.registerTileEntity(TileMilkJar.class, BlockMilkJar.registryName.toString());
		GameRegistry.registerTileEntity(TileCowJar.class, BlockCowJar.registryName.toString());
		GameRegistry.registerTileEntity(TileSpiceRack.class, BlockSpiceRack.registryName.toString());
		GameRegistry.registerTileEntity(TileCounter.class, BlockCounter.registryName.toString());
		GameRegistry.registerTileEntity(TileCorner.class, BlockCorner.registryName.toString());
	}
}
