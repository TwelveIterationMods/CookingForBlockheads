package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ModTileEntities {

    public static TileEntityType<CookingTableTileEntity> cookingTable;
    public static TileEntityType<OvenTileEntity> oven;
    public static TileEntityType<FridgeTileEntity> fridge;
    public static TileEntityType<TileSink> sink;
    public static TileEntityType<ToolRackTileEntity> toolRack;
    public static TileEntityType<ToasterTileEntity> toaster;
    public static TileEntityType<TileMilkJar> milkJar;
    public static TileEntityType<TileCowJar> cowJar;
    public static TileEntityType<SpiceRackTileEntity> spiceRack;
    public static TileEntityType<CounterTileEntity> counter;
    public static TileEntityType<CabinetTileEntity> cabinet;
    public static TileEntityType<CornerTileEntity> corner;
    public static TileEntityType<FruitBasketTileEntity> fruitBasket;
    public static TileEntityType<CuttingBoardTileEntity> cuttingBoard;

    public static void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
        registry.registerAll(
                cookingTable = build(CookingTableTileEntity::new, "cooking_table", ModBlocks.cookingTable),
                oven = build(OvenTileEntity::new, ModBlocks.oven),
                fridge = build(FridgeTileEntity::new, "fridge", ModBlocks.fridge),
                sink = build(TileSink::new, "sink", ModBlocks.sink),
                toolRack = build(ToolRackTileEntity::new, ModBlocks.toolRack),
                toaster = build(ToasterTileEntity::new, ModBlocks.toaster),
                milkJar = build(TileMilkJar::new, ModBlocks.milkJar),
                cowJar = build(TileCowJar::new, ModBlocks.cowJar),
                spiceRack = build(SpiceRackTileEntity::new, ModBlocks.spiceRack),
                counter = build(CounterTileEntity::new, "counter", ModBlocks.counter),
                cabinet = build(CabinetTileEntity::new, "cabinet", ModBlocks.cabinet),
                corner = build(CornerTileEntity::new, "corner", ModBlocks.corner),
                fruitBasket = build(FruitBasketTileEntity::new, ModBlocks.fruitBasket),
                cuttingBoard = build(CuttingBoardTileEntity::new, ModBlocks.cuttingBoard)
        );
    }

    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, String name, List<Block> block) {
        return build(factory, new ResourceLocation(CookingForBlockheads.MOD_ID, name), block.toArray(new Block[0]));
    }

    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, String name, Block block) {
        return build(factory, new ResourceLocation(CookingForBlockheads.MOD_ID, name), block);
    }

    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, Block block) {
        return build(factory, Objects.requireNonNull(block.getRegistryName()), block);
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, ResourceLocation registryName, Block... block) {
        //noinspection ConstantConditions dataFixerType can be null apparently
        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(null).setRegistryName(registryName);
    }
}
