package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static Block cookingTable;
    public static Block oven;
    public static Block fridge;
    public static Block sink;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block counter;
    public static Block cabinet;
    public static Block corner;
    public static Block kitchenFloor;
    public static Block fruitBasket;
    public static Block cuttingBoard;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                cookingTable = new BlockCookingTable().setRegistryName(BlockCookingTable.name),
                oven = new BlockOven().setRegistryName(BlockOven.name),
                fridge = new BlockFridge().setRegistryName(BlockFridge.name),
                sink = new BlockSink().setRegistryName(BlockSink.name),
                toolRack = new BlockToolRack().setRegistryName(BlockToolRack.name),
                toaster = new BlockToaster().setRegistryName(BlockToaster.name),
                milkJar = new BlockMilkJar().setRegistryName(BlockMilkJar.name),
                cowJar = new BlockCowJar().setRegistryName(BlockCowJar.name),
                spiceRack = new BlockSpiceRack().setRegistryName(BlockSpiceRack.name),
                counter = new BlockCounter().setRegistryName(BlockCounter.name),
                cabinet = new BlockCabinet().setRegistryName(BlockCabinet.name),
                corner = new BlockCorner().setRegistryName(BlockCorner.name),
                kitchenFloor = new BlockKitchenFloor().setRegistryName(BlockKitchenFloor.name),
                fruitBasket = new BlockFruitBasket().setRegistryName(BlockFruitBasket.name),
                cuttingBoard = new BlockCuttingBoard().setRegistryName(BlockCuttingBoard.name)
        );
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new BlockItem(ModBlocks.cookingTable, createItemProperties()).setRegistryName(BlockCookingTable.name),
                new BlockItem(ModBlocks.oven, createItemProperties()).setRegistryName(BlockOven.name),
                new BlockItem(ModBlocks.fridge, createItemProperties()).setRegistryName(BlockFridge.name),
                new BlockItem(ModBlocks.sink, createItemProperties()).setRegistryName(BlockSink.name),
                new BlockItem(ModBlocks.toolRack, createItemProperties()).setRegistryName(BlockToolRack.name),
                new BlockItem(ModBlocks.toaster, createItemProperties()).setRegistryName(BlockToaster.name),
                new BlockItem(ModBlocks.milkJar, createItemProperties()).setRegistryName(BlockMilkJar.name),
                new BlockItem(ModBlocks.cowJar, createItemProperties()).setRegistryName(BlockCowJar.name),
                new BlockItem(ModBlocks.spiceRack, createItemProperties()).setRegistryName(BlockSpiceRack.name),
                new BlockItem(ModBlocks.counter, createItemProperties()).setRegistryName(BlockCounter.name),
                new BlockItem(ModBlocks.cabinet, createItemProperties()).setRegistryName(BlockCabinet.name),
                new BlockItem(ModBlocks.corner, createItemProperties()).setRegistryName(BlockCorner.name),
                new BlockItem(ModBlocks.kitchenFloor, createItemProperties()).setRegistryName(BlockKitchenFloor.name),
                new BlockItem(ModBlocks.fruitBasket, createItemProperties()).setRegistryName(BlockFruitBasket.name),
                new BlockItem(ModBlocks.cuttingBoard, new Item.Properties()).setRegistryName(BlockCuttingBoard.name)
        );
    }

    private static Item.Properties createItemProperties() {
        return new Item.Properties().group(CookingForBlockheads.itemGroup);
    }

}
