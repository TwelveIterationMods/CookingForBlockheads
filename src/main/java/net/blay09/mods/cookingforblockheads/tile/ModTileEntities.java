package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ModTileEntities {

    public static TileEntityType<TileCookingTable> cookingTable;
    public static TileEntityType<TileOven> oven;
    public static TileEntityType<TileFridge> fridge;
    public static TileEntityType<TileSink> sink;
    public static TileEntityType<TileToolRack> toolRack;
    public static TileEntityType<TileToaster> toaster;
    public static TileEntityType<TileMilkJar> milkJar;
    public static TileEntityType<TileCowJar> cowJar;
    public static TileEntityType<SpiceRackTileEntity> spiceRack;
    public static TileEntityType<TileCounter> counter;
    public static TileEntityType<TileCabinet> cabinet;
    public static TileEntityType<TileCorner> corner;
    public static TileEntityType<TileFruitBasket> fruitBasket;
    public static TileEntityType<TileCuttingBoard> cuttingBoard;

    public static void registerTileEntities(IForgeRegistry<TileEntityType<?>> registry) {
        registry.registerAll(
                cookingTable = build(TileCookingTable::new, ModBlocks.cookingTable),
                oven = build(TileOven::new, ModBlocks.oven),
                fridge = build(TileFridge::new, ModBlocks.fridge),
                sink = build(TileSink::new, ModBlocks.sink),
                toolRack = build(TileToolRack::new, ModBlocks.toolRack),
                toaster = build(TileToaster::new, ModBlocks.toaster),
                milkJar = build(TileMilkJar::new, ModBlocks.milkJar),
                cowJar = build(TileCowJar::new, ModBlocks.cowJar),
                spiceRack = build(SpiceRackTileEntity::new, ModBlocks.spiceRack),
                counter = build(TileCounter::new, ModBlocks.counter),
                cabinet = build(TileCabinet::new, ModBlocks.cabinet),
                corner = build(TileCorner::new, ModBlocks.corner),
                fruitBasket = build(TileFruitBasket::new, ModBlocks.fruitBasket),
                cuttingBoard = build(TileCuttingBoard::new, ModBlocks.cuttingBoard)
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
