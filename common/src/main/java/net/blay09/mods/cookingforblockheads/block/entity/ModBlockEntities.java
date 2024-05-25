package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;

public class ModBlockEntities {

    public static DeferredObject<BlockEntityType<CookingTableBlockEntity>> cookingTable;
    public static DeferredObject<BlockEntityType<OvenBlockEntity>> oven;
    public static DeferredObject<BlockEntityType<FridgeBlockEntity>> fridge;
    public static DeferredObject<BlockEntityType<SinkBlockEntity>> sink;
    public static DeferredObject<BlockEntityType<ToolRackBlockEntity>> toolRack;
    public static DeferredObject<BlockEntityType<ToasterBlockEntity>> toaster;
    public static DeferredObject<BlockEntityType<MilkJarBlockEntity>> milkJar;
    public static DeferredObject<BlockEntityType<CowJarBlockEntity>> cowJar;
    public static DeferredObject<BlockEntityType<SpiceRackBlockEntity>> spiceRack;
    public static DeferredObject<BlockEntityType<CounterBlockEntity>> counter;
    public static DeferredObject<BlockEntityType<CabinetBlockEntity>> cabinet;
    public static DeferredObject<BlockEntityType<FruitBasketBlockEntity>> fruitBasket;
    public static DeferredObject<BlockEntityType<CuttingBoardBlockEntity>> cuttingBoard;

    public static void initialize(BalmBlockEntities blockEntities) {
        cookingTable = blockEntities.registerBlockEntity(id("cooking_table"), CookingTableBlockEntity::new, () -> {
            final var allCookingTables = Arrays.copyOf(ModBlocks.dyedCookingTables, ModBlocks.dyedCookingTables.length + 1);
            allCookingTables[allCookingTables.length - 1] = ModBlocks.cookingTable;
            return allCookingTables;
        });
        oven = blockEntities.registerBlockEntity(id("oven"), OvenBlockEntity::new, () -> ModBlocks.ovens);
        fridge = blockEntities.registerBlockEntity(id("fridge"), FridgeBlockEntity::new, () -> new Block[]{ModBlocks.fridge});
        sink = blockEntities.registerBlockEntity(id("sink"), SinkBlockEntity::new, () -> new Block[]{ModBlocks.sink});
        toolRack = blockEntities.registerBlockEntity(id("tool_rack"), ToolRackBlockEntity::new, () -> new Block[]{ModBlocks.toolRack});
        toaster = blockEntities.registerBlockEntity(id("toaster"), ToasterBlockEntity::new, () -> new Block[]{ModBlocks.toaster});
        milkJar = blockEntities.registerBlockEntity(id("milk_jar"), MilkJarBlockEntity::new, () -> new Block[]{ModBlocks.milkJar});
        cowJar = blockEntities.registerBlockEntity(id("cow_jar"), CowJarBlockEntity::new, () -> new Block[]{ModBlocks.cowJar});
        spiceRack = blockEntities.registerBlockEntity(id("spice_rack"), SpiceRackBlockEntity::new, () -> new Block[]{ModBlocks.spiceRack});
        counter = blockEntities.registerBlockEntity(id("counter"), CounterBlockEntity::new, () -> {
            final var allCounters = Arrays.copyOf(ModBlocks.dyedCounters, ModBlocks.dyedCounters.length + 1);
            allCounters[allCounters.length - 1] = ModBlocks.counter;
            return allCounters;
        });
        cabinet = blockEntities.registerBlockEntity(id("cabinet"), CabinetBlockEntity::new, () -> {
            final var allCabinets = Arrays.copyOf(ModBlocks.dyedCabinets, ModBlocks.dyedCabinets.length + 1);
            allCabinets[allCabinets.length - 1] = ModBlocks.cabinet;
            return allCabinets;
        });
        fruitBasket = blockEntities.registerBlockEntity(id("fruit_basket"), FruitBasketBlockEntity::new, () -> new Block[]{ModBlocks.fruitBasket});
        cuttingBoard = blockEntities.registerBlockEntity(id("cutting_board"), CuttingBoardBlockEntity::new, () -> new Block[]{ModBlocks.cuttingBoard});
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
