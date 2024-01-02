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

    public static Block oven;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block fruitBasket;
    public static Block cuttingBoard;
    public static Block cookingTable;
    public static Block fridge;
    public static Block sink;
    public static Block counter;
    public static Block cabinet;
    public static Block corner;
    public static Block hangingCorner;
    public static Block[] kitchenFloors = new Block[DyeColor.values().length];

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> oven = new OvenBlock(defaultProperties()), () -> itemBlock(oven), id("oven"));
        blocks.register(() -> toolRack = new ToolRackBlock(defaultProperties()), () -> itemBlock(toolRack), id("tool_rack"));
        blocks.register(() -> toaster = new ToasterBlock(defaultProperties()), () -> itemBlock(toaster), id("toaster"));
        blocks.register(() -> milkJar = new MilkJarBlock(defaultProperties()), () -> itemBlock(milkJar), id("milk_jar"));
        blocks.register(() -> cowJar = new CowJarBlock(defaultProperties()), () -> itemBlock(cowJar), id("cow_jar"));
        blocks.register(() -> spiceRack = new SpiceRackBlock(defaultProperties()), () -> itemBlock(spiceRack), id("spice_rack"));
        blocks.register(() -> fruitBasket = new FruitBasketBlock(defaultProperties()), () -> itemBlock(fruitBasket), id("fruit_basket"));
        blocks.registerBlock(() -> cuttingBoard = new CuttingBoardBlock(defaultProperties()), id("cutting_board"));
        blocks.register(() -> cookingTable = new CookingTableBlock(defaultProperties()), () -> itemBlock(cookingTable), id("cooking_table"));
        blocks.register(() -> fridge = new FridgeBlock(defaultProperties()), () -> itemBlock(fridge), id("fridge"));
        blocks.register(() -> sink = new SinkBlock(defaultProperties()), () -> itemBlock(sink), id("sink"));
        blocks.register(() -> counter = new CounterBlock(defaultProperties()), () -> itemBlock(counter), id("counter"));
        blocks.register(() -> cabinet = new CabinetBlock(defaultProperties()), () -> itemBlock(cabinet), id("cabinet"));
        blocks.register(() -> corner = new CornerBlock(defaultProperties()), () -> itemBlock(corner), id("corner"));
        blocks.register(() -> hangingCorner = new HangingCornerBlock(defaultProperties()), () -> itemBlock(hangingCorner), id("hanging_corner"));

        DyeColor[] colors = DyeColor.values();
        kitchenFloors = new Block[colors.length];
        for (DyeColor color : colors) {
            blocks.register(() -> kitchenFloors[color.ordinal()] = new KitchenFloorBlock(defaultProperties()), () -> itemBlock(kitchenFloors[color.ordinal()]), id(color.getSerializedName() + "_kitchen_floor"));
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
