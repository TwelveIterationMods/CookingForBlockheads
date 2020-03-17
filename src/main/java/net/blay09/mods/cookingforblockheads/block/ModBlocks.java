package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;
import java.util.Objects;

public class ModBlocks {

    public static Block oven;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block[] kitchenFloors;
    public static Block fruitBasket;
    public static Block cuttingBoard;
    public static Block cookingTable;
    public static Block fridge;
    public static Block sink;
    public static Block counter;
    public static Block cabinet;
    public static Block corner;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                cookingTable = new CookingTableBlock().setRegistryName(CookingTableBlock.name),
                fridge = new FridgeBlock().setRegistryName(FridgeBlock.name),
                sink = new SinkBlock().setRegistryName(SinkBlock.name),
                counter = new KitchenCounterBlock().setRegistryName(KitchenCounterBlock.name),
                cabinet = new CabinetBlock().setRegistryName(CabinetBlock.name),
                corner = new KitchenCornerBlock().setRegistryName(KitchenCornerBlock.name),
                oven = new OvenBlock().setRegistryName(OvenBlock.name),
                toolRack = new ToolRackBlock().setRegistryName(ToolRackBlock.name),
                toaster = new ToasterBlock().setRegistryName(ToasterBlock.name),
                milkJar = new MilkJarBlock().setRegistryName(MilkJarBlock.name),
                cowJar = new CowJarBlock().setRegistryName(CowJarBlock.name),
                spiceRack = new SpiceRackBlock().setRegistryName(SpiceRackBlock.name),
                fruitBasket = new FruitBasketBlock().setRegistryName(FruitBasketBlock.name),
                cuttingBoard = new CuttingBoardBlock().setRegistryName(CuttingBoardBlock.name)
        );

        DyeColor[] colors = DyeColor.values();
        kitchenFloors = new Block[colors.length];
        for (DyeColor color : colors) {
            registry.register(kitchenFloors[color.ordinal()] = new KitchenFloorBlock().setRegistryName(color.name().toLowerCase(Locale.ENGLISH) + "_" + KitchenFloorBlock.name));
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                new BlockItem(ModBlocks.cookingTable, createItemProperties()).setRegistryName(CookingTableBlock.name),
                new BlockItem(ModBlocks.oven, createItemProperties()).setRegistryName(OvenBlock.name),
                new BlockItem(ModBlocks.sink, createItemProperties()).setRegistryName(SinkBlock.name),
                new BlockItem(ModBlocks.toolRack, createItemProperties()).setRegistryName(ToolRackBlock.name),
                new BlockItem(ModBlocks.toaster, createItemProperties()).setRegistryName(ToasterBlock.name),
                new BlockItem(ModBlocks.milkJar, createItemProperties()).setRegistryName(MilkJarBlock.name),
                new BlockItem(ModBlocks.cowJar, createItemProperties()).setRegistryName(CowJarBlock.name),
                new BlockItem(ModBlocks.spiceRack, createItemProperties()).setRegistryName(SpiceRackBlock.name),
                new BlockItem(ModBlocks.fruitBasket, createItemProperties()).setRegistryName(FruitBasketBlock.name),
                new BlockItem(ModBlocks.counter, createItemProperties()).setRegistryName(KitchenCounterBlock.name),
                new BlockItem(ModBlocks.corner, createItemProperties()).setRegistryName(KitchenCornerBlock.name),
                new BlockItem(ModBlocks.cabinet, createItemProperties()).setRegistryName(CabinetBlock.name),
                new BlockItem(ModBlocks.fridge, createItemProperties()).setRegistryName(FridgeBlock.name)
        );

        for (Block kitchenFloor : kitchenFloors) {
            registry.register(new BlockItem(kitchenFloor, createItemProperties()).setRegistryName(Objects.requireNonNull(kitchenFloor.getRegistryName())));
        }
    }

    private static Item.Properties createItemProperties() {
        return new Item.Properties().group(CookingForBlockheads.itemGroup);
    }

}
