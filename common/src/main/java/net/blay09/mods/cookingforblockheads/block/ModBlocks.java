package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.blay09.mods.cookingforblockheads.component.ModDataComponents;
import net.blay09.mods.cookingforblockheads.component.MultiblockKitchenComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Function;

public class ModBlocks {

    public static DiscriminatedBlocks<DyeColor> cookingTables;
    public static DiscriminatedBlocks<DyeColor> counters;
    public static DiscriminatedBlocks<DyeColor> cabinets;
    public static DiscriminatedBlocks<DyeColor> ovens;
    public static DeferredBlock toolRack;
    public static DeferredBlock toaster;
    public static DeferredBlock milkJar;
    public static DeferredBlock cowJar;
    public static DeferredBlock spiceRack;
    public static DeferredBlock fruitBasket;
    public static DeferredBlock cuttingBoard;
    public static DiscriminatedBlocks<DyeColor> fridges;
    public static DiscriminatedBlocks<DyeColor> sinks;
    public static DiscriminatedBlocks<DyeColor> connectors;
    public static DiscriminatedBlocks<DyeColor> kitchenFloors;

    private static Function<Item.Properties, Item.Properties> configureTooltip(Component component) {
        return (Item.Properties properties) ->
                properties.component(ModDataComponents.multiblockKitchen.value(), new MultiblockKitchenComponent(component));
    }

    public static void initialize(BalmBlockRegistrar blocks) {
        toolRack = blocks.register("tool_rack", ToolRackBlock::new, it -> it.sound(SoundType.WOOD).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.tool_rack.description")))
                .asDeferredBlock();

        toaster = blocks.register("toaster", ToasterBlock::new, it -> it.sound(SoundType.METAL).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.toaster.description")))
                .asDeferredBlock();

        milkJar = blocks.register("milk_jar", MilkJarBlock::new, it -> it.sound(SoundType.GLASS).strength(0.6f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.milk_jar.description")))
                .asDeferredBlock();

        cowJar = blocks.register("cow_jar", CowJarBlock::new, it -> it.sound(SoundType.GLASS).strength(0.6f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cow_jar.description")))
                .asDeferredBlock();

        spiceRack = blocks.register("spice_rack", SpiceRackBlock::new, it -> it.sound(SoundType.WOOD).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.spice_rack.description")))
                .asDeferredBlock();

        fruitBasket = blocks.register("fruit_basket", FruitBasketBlock::new, it -> it.sound(SoundType.WOOD).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.fruit_basket.description")))
                .asDeferredBlock();

        cuttingBoard = blocks.register("cutting_board", CuttingBoardBlock::new, it -> it.sound(SoundType.WOOD).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cutting_board.description")))
                .asDeferredBlock();

        final var colors = DyeColor.values();
        ovens = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "oven"),
                        OvenBlock::new,
                        it -> it.sound(SoundType.METAL).strength(5f, 10f))
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.oven.description"))))
                .asDiscriminatedBlocks();

        fridges = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "fridge"),
                        FridgeBlock::new,
                        it -> it.sound(SoundType.METAL).strength(5f, 10f))
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.fridge.description")))).
                asDiscriminatedBlocks();

        connectors = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "connector"),
                        ConnectorBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withNullDiscriminator()
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cooking_table.description"))))
                .asDiscriminatedBlocks();

        kitchenFloors = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "kitchen_floor"),
                        KitchenFloorBlock::new,
                        it -> it.sound(SoundType.STONE).strength(0.8f))
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.kitchen_floor.description"))))
                .asDiscriminatedBlocks();

        cookingTables = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "cooking_table"),
                        CookingTableBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withNullDiscriminator()
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cooking_table.description"))))
                .asDiscriminatedBlocks();

        counters = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "counter"),
                        CounterBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withNullDiscriminator()
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.counter.description"))))
                .asDiscriminatedBlocks();

        cabinets = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "cabinet"),
                        CabinetBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withNullDiscriminator()
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cabinet.description"))))
                .asDiscriminatedBlocks();

        sinks = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "sink"),
                        SinkBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withNullDiscriminator()
                .forEach(it -> it.withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.sink.description"))))
                .asDiscriminatedBlocks();
    }

}
