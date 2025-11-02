package net.blay09.mods.cookingforblockheads.block.entity;

import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static Holder<BlockEntityType<CookingTableBlockEntity>> cookingTable;
    public static Holder<BlockEntityType<OvenBlockEntity>> oven;
    public static Holder<BlockEntityType<FridgeBlockEntity>> fridge;
    public static Holder<BlockEntityType<SinkBlockEntity>> sink;
    public static Holder<BlockEntityType<ToolRackBlockEntity>> toolRack;
    public static Holder<BlockEntityType<ToasterBlockEntity>> toaster;
    public static Holder<BlockEntityType<MilkJarBlockEntity>> milkJar;
    public static Holder<BlockEntityType<CowJarBlockEntity>> cowJar;
    public static Holder<BlockEntityType<SpiceRackBlockEntity>> spiceRack;
    public static Holder<BlockEntityType<CounterBlockEntity>> counter;
    public static Holder<BlockEntityType<CabinetBlockEntity>> cabinet;
    public static Holder<BlockEntityType<FruitBasketBlockEntity>> fruitBasket;
    public static Holder<BlockEntityType<CuttingBoardBlockEntity>> cuttingBoard;

    public static void initialize(BalmBlockEntityTypeRegistrar blockEntityTypes) {
        cookingTable = blockEntityTypes.register("cooking_table", CookingTableBlockEntity::new, ModBlocks.cookingTables.values()).asHolder();
        oven = blockEntityTypes.register("oven", OvenBlockEntity::new, ModBlocks.ovens.values()).asHolder();
        fridge = blockEntityTypes.register("fridge", FridgeBlockEntity::new, ModBlocks.fridges.values()).asHolder();
        sink = blockEntityTypes.register("sink", SinkBlockEntity::new, ModBlocks.sinks.values()).asHolder();
        toolRack = blockEntityTypes.register("tool_rack", ToolRackBlockEntity::new, ModBlocks.toolRack).asHolder();
        toaster = blockEntityTypes.register("toaster", ToasterBlockEntity::new, ModBlocks.toaster).asHolder();
        milkJar = blockEntityTypes.register("milk_jar", MilkJarBlockEntity::new, ModBlocks.milkJar).asHolder();
        cowJar = blockEntityTypes.register("cow_jar", CowJarBlockEntity::new, ModBlocks.cowJar).asHolder();
        spiceRack = blockEntityTypes.register("spice_rack", SpiceRackBlockEntity::new, ModBlocks.spiceRack).asHolder();
        counter = blockEntityTypes.register("counter", CounterBlockEntity::new, ModBlocks.counters.values()).asHolder();
        cabinet = blockEntityTypes.register("cabinet", CabinetBlockEntity::new, ModBlocks.cabinets.values()).asHolder();
        fruitBasket = blockEntityTypes.register("fruit_basket", FruitBasketBlockEntity::new, ModBlocks.fruitBasket).asHolder();
        cuttingBoard = blockEntityTypes.register("cutting_board", CuttingBoardBlockEntity::new, ModBlocks.cuttingBoard).asHolder();
    }

}
