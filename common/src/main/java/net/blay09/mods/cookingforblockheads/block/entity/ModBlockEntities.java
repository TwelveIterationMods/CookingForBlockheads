package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;

public class ModBlockEntities {

    public static BalmBlockEntities blockEntities = Balm.getBlockEntities();
    public static DeferredObject<BlockEntityType<CookingTableBlockEntity>> cookingTable = blockEntities.registerBlockEntity(id("cooking_table"),
            CookingTableBlockEntity::new,
            () -> {
                final var allCookingTables = Arrays.copyOf(ModBlocks.dyedCookingTables, ModBlocks.dyedCookingTables.length + 1);
                allCookingTables[allCookingTables.length - 1] = ModBlocks.cookingTable;
                return allCookingTables;
            });
    public static DeferredObject<BlockEntityType<OvenBlockEntity>> oven = blockEntities.registerBlockEntity(id("oven"),
            OvenBlockEntity::new,
            () -> ModBlocks.ovens);
    public static DeferredObject<BlockEntityType<FridgeBlockEntity>> fridge = blockEntities.registerBlockEntity(id("fridge"),
            FridgeBlockEntity::new,
            () -> ModBlocks.fridges);
    public static DeferredObject<BlockEntityType<SinkBlockEntity>> sink = blockEntities.registerBlockEntity(id("sink"), SinkBlockEntity::new, () -> {
        final var allSinks = Arrays.copyOf(ModBlocks.dyedSinks, ModBlocks.dyedSinks.length + 1);
        allSinks[allSinks.length - 1] = ModBlocks.sink;
        return allSinks;
    });
    public static DeferredObject<BlockEntityType<ToolRackBlockEntity>> toolRack = blockEntities.registerBlockEntity(id("tool_rack"),
            ToolRackBlockEntity::new,
            () -> new Block[]{ModBlocks.toolRack});
    public static DeferredObject<BlockEntityType<ToasterBlockEntity>> toaster = blockEntities.registerBlockEntity(id("toaster"),
            ToasterBlockEntity::new,
            () -> new Block[]{ModBlocks.toaster});
    public static DeferredObject<BlockEntityType<MilkJarBlockEntity>> milkJar = blockEntities.registerBlockEntity(id("milk_jar"),
            MilkJarBlockEntity::new,
            () -> new Block[]{ModBlocks.milkJar});
    public static DeferredObject<BlockEntityType<CowJarBlockEntity>> cowJar = blockEntities.registerBlockEntity(id("cow_jar"),
            CowJarBlockEntity::new,
            () -> new Block[]{ModBlocks.cowJar});
    public static DeferredObject<BlockEntityType<SpiceRackBlockEntity>> spiceRack = blockEntities.registerBlockEntity(id("spice_rack"),
            SpiceRackBlockEntity::new,
            () -> new Block[]{ModBlocks.spiceRack});
    public static DeferredObject<BlockEntityType<CounterBlockEntity>> counter = blockEntities.registerBlockEntity(id("counter"),
            CounterBlockEntity::new,
            () -> {
                final var allCounters = Arrays.copyOf(ModBlocks.dyedCounters, ModBlocks.dyedCounters.length + 1);
                allCounters[allCounters.length - 1] = ModBlocks.counter;
                return allCounters;
            });
    public static DeferredObject<BlockEntityType<CabinetBlockEntity>> cabinet = blockEntities.registerBlockEntity(id("cabinet"),
            CabinetBlockEntity::new,
            () -> {
                final var allCabinets = Arrays.copyOf(ModBlocks.dyedCabinets, ModBlocks.dyedCabinets.length + 1);
                allCabinets[allCabinets.length - 1] = ModBlocks.cabinet;
                return allCabinets;
            });
    public static DeferredObject<BlockEntityType<FruitBasketBlockEntity>> fruitBasket = blockEntities.registerBlockEntity(id("fruit_basket"),
            FruitBasketBlockEntity::new,
            () -> new Block[]{ModBlocks.fruitBasket});
    public static DeferredObject<BlockEntityType<CuttingBoardBlockEntity>> cuttingBoard = blockEntities.registerBlockEntity(id("cutting_board"),
            CuttingBoardBlockEntity::new,
            () -> new Block[]{ModBlocks.cuttingBoard});

    public static void initialize() {
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, name);
    }

}
