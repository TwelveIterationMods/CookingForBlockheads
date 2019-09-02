package net.blay09.mods.cookingforblockheads.container;

import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ModContainers {

    public static ContainerType<CounterContainer> counter;
    public static ContainerType<FridgeContainer> fridge;
    public static ContainerType<FruitBasketContainer> fruitBasket;
    public static ContainerType<OvenContainer> oven;
    public static ContainerType<SpiceRackContainer> spiceRack;
    public static ContainerType<RecipeBookContainer> recipeBook;

    public static void registerContainers(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(counter = register("counter", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileCounter) {
                return new CounterContainer(windowId, inv, (TileCounter) tileEntity);
            }

            return null;
        })));

        registry.register(fridge = register("fridge", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileFridge) {
                return new FridgeContainer(windowId, inv, (TileFridge) tileEntity);
            }

            return null;
        })));

        registry.register(fruitBasket = register("fruit_basket", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileFruitBasket) {
                return new FruitBasketContainer(windowId, inv, (TileFruitBasket) tileEntity);
            }

            return null;
        })));

        registry.register(oven = register("oven", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileOven) {
                return new OvenContainer(windowId, inv, (TileOven) tileEntity);
            }

            return null;
        })));

        registry.register(spiceRack = register("spice_rack", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof SpiceRackTileEntity) {
                return new SpiceRackContainer(windowId, inv, (SpiceRackTileEntity) tileEntity);
            }

            return null;
        })));

        registry.register(recipeBook = register("cooking_table", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof TileCookingTable) {
                RecipeBookContainer container = new RecipeBookContainer(inv.player);
                container.setKitchenMultiBlock(null); // TODO See GuiHandler
                return container;
            }

            return null;
        })));

        registry.register(recipeBook = register("recipe_book", ((windowId, inv, data) -> new RecipeBookContainer(inv.player))));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
