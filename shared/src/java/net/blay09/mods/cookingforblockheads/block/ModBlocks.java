package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class ModBlocks {

    public static Block oven = new OvenBlock();
    public static Block toolRack = new ToolRackBlock();
    public static Block toaster = new ToasterBlock();
    public static Block milkJar = new MilkJarBlock();
    public static Block cowJar = new CowJarBlock();
    public static Block spiceRack = new SpiceRackBlock();
    public static Block[] kitchenFloors = new Block[DyeColor.values().length];
    public static Block fruitBasket = new FruitBasketBlock();
    public static Block cuttingBoard = new CuttingBoardBlock();
    public static Block cookingTable = new CookingTableBlock();
    public static Block fridge = new FridgeBlock();
    public static Block sink = new SinkBlock();
    public static Block counter = new KitchenCounterBlock();
    public static Block cabinet = new CabinetBlock();
    public static Block corner = new KitchenCornerBlock();
    public static Block hangingCorner = new HangingCornerBlock();

    public static void initialize(BalmBlocks blocks) {
        blocks.register(() -> cookingTable, () -> itemBlock(cookingTable), id("cooking_table"));
        blocks.register(() -> fridge, () -> itemBlock(fridge), id("fridge"));
        blocks.register(() -> sink, () -> itemBlock(sink), id("sink"));
        blocks.register(() -> counter, () -> itemBlock(counter), id("counter"));
        blocks.register(() -> cabinet, () -> itemBlock(cabinet), id("cabinet"));
        blocks.register(() -> corner, () -> itemBlock(corner), id("corner"));
        blocks.register(() -> oven, () -> itemBlock(oven), id("oven"));
        blocks.register(() -> toolRack, () -> itemBlock(toolRack), id("tool_rack"));
        blocks.register(() -> toaster, () -> itemBlock(toaster), id("toaster"));
        blocks.register(() -> milkJar, () -> itemBlock(milkJar), id("milk_jar"));
        blocks.register(() -> cowJar, () -> itemBlock(cowJar), id("cow_jar"));
        blocks.register(() -> spiceRack, () -> itemBlock(spiceRack), id("spice_rack"));
        blocks.register(() -> fruitBasket, () -> itemBlock(fruitBasket), id("fruit_basket"));
        blocks.register(() -> cuttingBoard, () -> itemBlock(cuttingBoard), id("cutting_board"));
        blocks.registerBlock(() -> cuttingBoard, id("cutting_board"));
        blocks.register(() -> hangingCorner, () -> itemBlock(hangingCorner), id("hanging_corner"));

        DyeColor[] colors = DyeColor.values();
        kitchenFloors = new Block[colors.length];
        for (DyeColor color : colors) {
            kitchenFloors[color.ordinal()] = new KitchenFloorBlock();
            blocks.register(() -> kitchenFloors[color.ordinal()], () -> itemBlock(kitchenFloors[color.ordinal()]), id(color.getSerializedName() + "_kitchen_floor"));
        }
    }
    private static BlockItem itemBlock(Block block) {
        return new BlockItem(block, Balm.getBlocks().itemProperties(ModItems.creativeModeTab));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

    private static BlockBehaviour.Properties defaultProperties() {
        return Balm.getBlocks().blockProperties(Material.STONE).sound(SoundType.STONE).strength(5f, 2000f);
    }

}
