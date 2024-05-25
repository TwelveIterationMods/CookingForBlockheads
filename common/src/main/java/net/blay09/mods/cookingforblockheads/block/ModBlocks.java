package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

    public static CookingTableBlock[] dyedCookingTables = new CookingTableBlock[DyeColor.values().length];
    public static CounterBlock[] dyedCounters = new CounterBlock[DyeColor.values().length];
    public static CabinetBlock[] dyedCabinets = new CabinetBlock[DyeColor.values().length];
    public static OvenBlock[] ovens = new OvenBlock[DyeColor.values().length];
    public static CookingTableBlock cookingTable;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block fruitBasket;
    public static Block cuttingBoard;
    public static Block fridge;
    public static Block sink;
    public static CounterBlock counter;
    public static CabinetBlock cabinet;
    public static Block connector;
    public static DyedConnectorBlock[] dyedConnectors = new DyedConnectorBlock[DyeColor.values().length];
    public static Block[] kitchenFloors = new Block[DyeColor.values().length];

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> toolRack = new ToolRackBlock(defaultProperties()), () -> itemBlock(toolRack), id("tool_rack"));
        blocks.register(() -> toaster = new ToasterBlock(defaultProperties()), () -> itemBlock(toaster), id("toaster"));
        blocks.register(() -> milkJar = new MilkJarBlock(defaultProperties()), () -> itemBlock(milkJar), id("milk_jar"));
        blocks.register(() -> cowJar = new CowJarBlock(defaultProperties()), () -> itemBlock(cowJar), id("cow_jar"));
        blocks.register(() -> spiceRack = new SpiceRackBlock(defaultProperties()), () -> itemBlock(spiceRack), id("spice_rack"));
        blocks.register(() -> fruitBasket = new FruitBasketBlock(defaultProperties()), () -> itemBlock(fruitBasket), id("fruit_basket"));
        blocks.register(() -> cuttingBoard = new CuttingBoardBlock(defaultProperties()), () -> itemBlock(cuttingBoard), id("cutting_board"));
        blocks.register(() -> fridge = new FridgeBlock(defaultProperties()), () -> itemBlock(fridge), id("fridge"));
        blocks.register(() -> sink = new SinkBlock(defaultProperties()), () -> itemBlock(sink), id("sink"));

        DyeColor[] colors = DyeColor.values();
        kitchenFloors = new Block[colors.length];
        ovens = new OvenBlock[colors.length];
        dyedCookingTables = new CookingTableBlock[colors.length];
        dyedConnectors = new DyedConnectorBlock[colors.length];
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            final var colorPrefixExceptWhite = color == DyeColor.WHITE ? "" : colorPrefix;
            blocks.register(() -> ovens[color.ordinal()] = new OvenBlock(color, defaultProperties()), () -> itemBlock(ovens[color.ordinal()]), id(colorPrefixExceptWhite + "oven"));
        }
        blocks.register(() -> connector = new ConnectorBlock(defaultProperties()), () -> itemBlock(connector), id("connector"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register(() -> dyedConnectors[color.ordinal()] = new DyedConnectorBlock(color, defaultProperties()), () -> itemBlock(dyedConnectors[color.ordinal()]), id(colorPrefix + "connector"));
        }
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register(() -> kitchenFloors[color.ordinal()] = new KitchenFloorBlock(defaultProperties()), () -> itemBlock(kitchenFloors[color.ordinal()]), id(colorPrefix + "kitchen_floor"));
        }
        blocks.register(() -> cookingTable = new CookingTableBlock(null, defaultProperties()), () -> itemBlock(cookingTable), id("cooking_table"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register(() -> dyedCookingTables[color.ordinal()] = new CookingTableBlock(color, defaultProperties()), () -> itemBlock(dyedCookingTables[color.ordinal()]), id(colorPrefix + "cooking_table"));
        }
        blocks.register(() -> counter = new CounterBlock(null, defaultProperties()), () -> itemBlock(counter), id("counter"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register(() -> dyedCounters[color.ordinal()] = new CounterBlock(color, defaultProperties()), () -> itemBlock(dyedCounters[color.ordinal()]), id(colorPrefix + "counter"));
        }
        blocks.register(() -> cabinet = new CabinetBlock(null, defaultProperties()), () -> itemBlock(cabinet), id("cabinet"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register(() -> dyedCabinets[color.ordinal()] = new CabinetBlock(color, defaultProperties()), () -> itemBlock(dyedCabinets[color.ordinal()]), id(colorPrefix + "cabinet"));
        }
    }

    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getItems().itemProperties());
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return Balm.getBlocks().blockProperties();
    }

}
