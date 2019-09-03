package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ModTileEntities {

    public static TileEntityType<CookingTableTileEntity> cookingTable;
    public static TileEntityType<TileOven> oven;
    public static TileEntityType<TileFridge> fridge;
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
                cookingTable = build(CookingTableTileEntity::new, ModBlocks.cookingTable),
                oven = build(TileOven::new, ModBlocks.oven),
                fridge = build(TileFridge::new, ModBlocks.fridge),
                sink = build(TileSink::new, ModBlocks.sink),
                toolRack = build(ToolRackTileEntity::new, ModBlocks.toolRack),
                toaster = build(ToasterTileEntity::new, ModBlocks.toaster),
                milkJar = build(TileMilkJar::new, ModBlocks.milkJar),
                cowJar = build(TileCowJar::new, ModBlocks.cowJar),
                spiceRack = build(SpiceRackTileEntity::new, ModBlocks.spiceRack),
                counter = build(CounterTileEntity::new, ModBlocks.counter),
                cabinet = build(CabinetTileEntity::new, ModBlocks.cabinet),
                corner = build(CornerTileEntity::new, ModBlocks.corner),
                fruitBasket = build(FruitBasketTileEntity::new, ModBlocks.fruitBasket),
                cuttingBoard = build(CuttingBoardTileEntity::new, ModBlocks.cuttingBoard)
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> TileEntityType<T> build(Supplier<T> factory, Block block) {
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            throw new IllegalArgumentException("Block passed into tile entity registration is not registered correctly");
        }

        //noinspection ConstantConditions dataFixerType can be null apparently
        return (TileEntityType<T>) TileEntityType.Builder.create(factory, block).build(null).setRegistryName(registryName);
    }
}
