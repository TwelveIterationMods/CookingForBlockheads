package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

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
    public static DeferredObject<BlockEntityType<CornerBlockEntity>> corner;
    public static DeferredObject<BlockEntityType<FruitBasketBlockEntity>> fruitBasket;
    public static DeferredObject<BlockEntityType<CuttingBoardBlockEntity>> cuttingBoard;

    public static void initialize(BalmBlockEntities blockEntities) {
        cookingTable = blockEntities.registerBlockEntity(id("cooking_table"), CookingTableBlockEntity::new, () -> ModBlocks.cookingTable);
        oven = blockEntities.registerBlockEntity(id("oven"), OvenBlockEntity::new, () -> ModBlocks.oven);
        fridge = blockEntities.registerBlockEntity(id("fridge"), FridgeBlockEntity::new, () -> ModBlocks.fridge);
        sink = blockEntities.registerBlockEntity(id("sink"), SinkBlockEntity::new, () -> ModBlocks.sink);
        toolRack = blockEntities.registerBlockEntity(id("tool_rack"), ToolRackBlockEntity::new, () -> ModBlocks.toolRack);
        toaster = blockEntities.registerBlockEntity(id("toaster"), ToasterBlockEntity::new, () -> ModBlocks.toaster);
        milkJar = blockEntities.registerBlockEntity(id("milk_jar"), MilkJarBlockEntity::new, () -> ModBlocks.milkJar);
        cowJar = blockEntities.registerBlockEntity(id("cow_jar"), CowJarBlockEntity::new, () -> ModBlocks.cowJar);
        spiceRack = blockEntities.registerBlockEntity(id("spice_rack"), SpiceRackBlockEntity::new, () -> ModBlocks.spiceRack);
        counter = blockEntities.registerBlockEntity(id("counter"), CounterBlockEntity::new, () -> ModBlocks.counter);
        cabinet = blockEntities.registerBlockEntity(id("cabinet"), CabinetBlockEntity::new, () -> ModBlocks.cabinet);
        corner = blockEntities.registerBlockEntity(id("corner"), CornerBlockEntity::new, () -> ModBlocks.corner);
        fruitBasket = blockEntities.registerBlockEntity(id("fruit_basket"), FruitBasketBlockEntity::new, () -> ModBlocks.fruitBasket);
        cuttingBoard = blockEntities.registerBlockEntity(id("cutting_board"), CuttingBoardBlockEntity::new, () -> ModBlocks.cuttingBoard);
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
