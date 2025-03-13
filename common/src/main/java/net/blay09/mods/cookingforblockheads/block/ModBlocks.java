package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.component.ModComponents;
import net.blay09.mods.cookingforblockheads.component.MultiblockKitchenComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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
    public static FridgeBlock[] fridges = new FridgeBlock[DyeColor.values().length];
    public static SinkBlock sink;
    public static SinkBlock[] dyedSinks = new SinkBlock[DyeColor.values().length];
    public static CounterBlock counter;
    public static CabinetBlock cabinet;
    public static Block connector;
    public static DyedConnectorBlock[] dyedConnectors = new DyedConnectorBlock[DyeColor.values().length];
    public static Block[] kitchenFloors = new Block[DyeColor.values().length];

    public static void initialize(BalmBlocks blocks) {
        blocks.register((identifier) -> toolRack = new ToolRackBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.tool_rack.description")))), id("tool_rack"));
        blocks.register((identifier) -> toaster = new ToasterBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.toaster.description")))), id("toaster"));
        blocks.register((identifier) -> milkJar = new MilkJarBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.milk_jar.description")))), id("milk_jar"));
        blocks.register((identifier) -> cowJar = new CowJarBlock(blockProperties(identifier)), ModBlocks::itemBlock, id("cow_jar"));
        blocks.register((identifier) -> spiceRack = new SpiceRackBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.spice_rack.description")))), id("spice_rack"));
        blocks.register((identifier) -> fruitBasket = new FruitBasketBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.fruit_basket.description")))),
                id("fruit_basket"));
        blocks.register((identifier) -> cuttingBoard = new CuttingBoardBlock(blockProperties(identifier)), ModBlocks::itemBlock, id("cutting_board"));

        DyeColor[] colors = DyeColor.values();
        kitchenFloors = new Block[colors.length];
        ovens = new OvenBlock[colors.length];
        dyedCookingTables = new CookingTableBlock[colors.length];
        dyedConnectors = new DyedConnectorBlock[colors.length];
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> ovens[color.ordinal()] = new OvenBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.oven.description")))),
                    id(colorPrefix + "oven"));
        }
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> fridges[color.ordinal()] = new FridgeBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.fridge.description")))),
                    id(colorPrefix + "fridge"));
        }
        blocks.register((identifier) -> connector = new ConnectorBlock(blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.connector.description")))), id("connector"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> dyedConnectors[color.ordinal()] = new DyedConnectorBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.connector.description")))),
                    id(colorPrefix + "connector"));
        }
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> kitchenFloors[color.ordinal()] = new KitchenFloorBlock(blockProperties(identifier)),
                    ModBlocks::itemBlock,
                    id(colorPrefix + "kitchen_floor"));
        }
        blocks.register((identifier) -> cookingTable = new CookingTableBlock(null, blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.cooking_table.description")))), id("cooking_table"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> dyedCookingTables[color.ordinal()] = new CookingTableBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.cooking_table.description")))),
                    id(colorPrefix + "cooking_table"));
        }
        blocks.register((identifier) -> counter = new CounterBlock(null, blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                        .component(ModComponents.multiblockKitchen.get(),
                                new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.counter.description")))),
                id("counter"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> dyedCounters[color.ordinal()] = new CounterBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.counter.description")))),
                    id(colorPrefix + "counter"));
        }
        blocks.register((identifier) -> cabinet = new CabinetBlock(null, blockProperties(identifier)), ModBlocks::itemBlock, id("cabinet"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> dyedCabinets[color.ordinal()] = new CabinetBlock(color, blockProperties(identifier)),
                    ModBlocks::itemBlock,
                    id(colorPrefix + "cabinet"));
        }
        blocks.register((identifier) -> sink = new SinkBlock(null, blockProperties(identifier)), (block, id) -> new BlockItem(block, itemProperties(id)
                .component(ModComponents.multiblockKitchen.get(),
                        new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.sink.description")))), id("sink"));
        for (final var color : colors) {
            final var colorPrefix = color.getSerializedName() + "_";
            blocks.register((identifier) -> dyedSinks[color.ordinal()] = new SinkBlock(color, blockProperties(identifier)),
                    (block, id) -> new BlockItem(block, itemProperties(id)
                            .component(ModComponents.multiblockKitchen.get(),
                                    new MultiblockKitchenComponent(Component.translatable("tooltip.cookingforblockheads.sink.description")))),
                    id(colorPrefix + "sink"));
        }
    }

    private static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of().setId(blockId(identifier));
    }

    private static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    private static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }

    private static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    private static BlockItem itemBlock(Block block, ResourceLocation identifier) {
        return new BlockItem(block, itemProperties(identifier));
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, name);
    }


}
