package net.blay09.mods.cookingforblockheads.container;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class ModContainers {

    public static ContainerType<CounterContainer> counter;
    public static ContainerType<FridgeContainer> fridge;
    public static ContainerType<FruitBasketContainer> fruitBasket;
    public static ContainerType<OvenContainer> oven;
    public static ContainerType<SpiceRackContainer> spiceRack;
    public static ContainerType<RecipeBookContainer> recipeBook;
    public static ContainerType<RecipeBookContainer> cookingTable;
    public static ContainerType<RecipeBookContainer> noFilterBook;
    public static ContainerType<RecipeBookContainer> craftingBook;

    public static void registerContainers(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(counter = register("counter", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new CounterContainer(windowId, inv, (CounterTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(fridge = register("fridge", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new FridgeContainer(windowId, inv, (TileFridge) Objects.requireNonNull(tileEntity));
        })));

        registry.register(fruitBasket = register("fruit_basket", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new FruitBasketContainer(windowId, inv, (FruitBasketTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(oven = register("oven", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new OvenContainer(windowId, inv, (OvenTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(spiceRack = register("spice_rack", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            return new SpiceRackContainer(windowId, inv, (SpiceRackTileEntity) Objects.requireNonNull(tileEntity));
        })));

        registry.register(cookingTable = register("cooking_table", ((windowId, inv, data) -> {
            World world = inv.player.world;
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = world.getTileEntity(pos);
            if (((CookingTableTileEntity) Objects.requireNonNull(tileEntity)).hasNoFilterBook()) {
                return new RecipeBookContainer(cookingTable, windowId, inv.player).setNoFilter().allowCrafting().setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(world, pos));
            } else {
                return new RecipeBookContainer(cookingTable, windowId, inv.player).allowCrafting().setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(world, pos));
            }
        })));

        registry.register(noFilterBook = register("no_filter_book", ((windowId, inv, data) -> new RecipeBookContainer(noFilterBook, windowId, inv.player).setNoFilter())));
        registry.register(recipeBook = register("recipe_book", ((windowId, inv, data) -> new RecipeBookContainer(recipeBook, windowId, inv.player))));
        registry.register(craftingBook = register("crafting_book", ((windowId, inv, data) -> new RecipeBookContainer(craftingBook, windowId, inv.player).allowCrafting())));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
