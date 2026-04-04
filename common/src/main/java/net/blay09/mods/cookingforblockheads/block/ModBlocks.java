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
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ModBlocks {

    public static final Set<DyeColor> supportedColors = Collections.synchronizedSet(new HashSet<>());

    static {
        supportedColors.add(DyeColor.WHITE);
        supportedColors.add(DyeColor.ORANGE);
        supportedColors.add(DyeColor.MAGENTA);
        supportedColors.add(DyeColor.LIGHT_BLUE);
        supportedColors.add(DyeColor.YELLOW);
        supportedColors.add(DyeColor.LIME);
        supportedColors.add(DyeColor.PINK);
        supportedColors.add(DyeColor.GRAY);
        supportedColors.add(DyeColor.LIGHT_GRAY);
        supportedColors.add(DyeColor.CYAN);
        supportedColors.add(DyeColor.PURPLE);
        supportedColors.add(DyeColor.BLUE);
        supportedColors.add(DyeColor.BROWN);
        supportedColors.add(DyeColor.GREEN);
        supportedColors.add(DyeColor.RED);
        supportedColors.add(DyeColor.BLACK);
    }

    public static DiscriminatedBlocks<DyeColor> cookingTables;
    public static DiscriminatedBlocks<DyeColor> counters;
    public static DiscriminatedBlocks<DyeColor> cabinets;
    public static DiscriminatedBlocks<DyeColor> ovens;
    public static DeferredBlock toolRack;
    public static DeferredBlock toaster;
    public static DeferredBlock milkJar;
    public static DeferredBlock cowJar;
    public static DeferredBlock cookieJar;
    public static DeferredBlock spiceRack;
    public static DeferredBlock fruitBasket;
    public static DeferredBlock cuttingBoard;
    public static DeferredBlock coffeeMachine;
    public static DiscriminatedBlocks<DyeColor> fridges;
    public static DiscriminatedBlocks<DyeColor> sinks;
    public static DiscriminatedBlocks<DyeColor> chickenSinks;
    public static DiscriminatedBlocks<DyeColor> connectors;
    public static DiscriminatedBlocks<DyeColor> kitchenFloors;

    private static Function<Item.Properties, Item.Properties> configureTooltip(Component component) {
        return (Item.Properties properties) ->
                properties.component(ModDataComponents.multiblockKitchen.value(), new MultiblockKitchenComponent(component));
    }

    public static void initialize(BalmBlockRegistrar blocks) {
        blocks.enableBlockDescriptionPrefixForItems();

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

        cookieJar = blocks.register("cookie_jar", CookieJarBlock::new, it -> it.sound(SoundType.GLASS).strength(0.6f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cookie_jar.description")))
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

        coffeeMachine = blocks.register("coffee_machine", CoffeeMachineBlock::new, it -> it.sound(SoundType.METAL).strength(2.5f))
                .withDefaultItem(configureTooltip(Component.translatable("tooltip.cookingforblockheads.coffee_machine.description")))
                .asDeferredBlock();

        final var colors = supportedColors;
        final var colorsWithNull = new HashSet<@Nullable DyeColor>(colors);
        colorsWithNull.add(null);
        ovens = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "oven"),
                        OvenBlock::new,
                        it -> it.sound(SoundType.METAL).strength(5f, 10f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.oven.description")))
                .asDiscriminatedBlocks();

        fridges = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "fridge"),
                        FridgeBlock::new,
                        it -> it.sound(SoundType.METAL).strength(5f, 10f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.fridge.description")))
                .asDiscriminatedBlocks();

        connectors = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "connector"),
                        ConnectorBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cooking_table.description")))
                .asDiscriminatedBlocks();

        kitchenFloors = blocks.registerDiscriminated(colors,
                        it -> DiscriminatedBlocks.prefix(it, "kitchen_floor"),
                        KitchenFloorBlock::new,
                        it -> it.sound(SoundType.STONE).strength(0.8f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.kitchen_floor.description")))
                .asDiscriminatedBlocks();

        cookingTables = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "cooking_table"),
                        CookingTableBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cooking_table.description")))
                .asDiscriminatedBlocks();

        counters = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "counter"),
                        CounterBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.counter.description")))
                .asDiscriminatedBlocks();

        cabinets = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "cabinet"),
                        CabinetBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.cabinet.description")))
                .asDiscriminatedBlocks();

        sinks = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "sink"),
                        SinkBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.sink.description")))
                .asDiscriminatedBlocks();

        chickenSinks = blocks.registerDiscriminated(colorsWithNull,
                        it -> DiscriminatedBlocks.prefix(it, "chicken_sink"),
                        ChickenSinkBlock::new,
                        it -> it.strength(1.25f, 4.2f))
                .withDefaultItems(configureTooltip(Component.translatable("tooltip.cookingforblockheads.chicken_sink.description")))
                .asDiscriminatedBlocks();
    }

}
