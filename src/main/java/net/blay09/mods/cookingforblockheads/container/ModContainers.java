package net.blay09.mods.cookingforblockheads.container;

import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
            if (tileEntity instanceof CounterTileEntity) {
                return new CounterContainer(windowId, inv, (CounterTileEntity) tileEntity);
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
            if (tileEntity instanceof FruitBasketTileEntity) {
                return new FruitBasketContainer(windowId, inv, (FruitBasketTileEntity) tileEntity);
            }

            return null;
        })));

        registry.register(oven = register("oven", ((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = inv.player.world.getTileEntity(pos);
            if (tileEntity instanceof OvenTileEntity) {
                return new OvenContainer(windowId, inv, (OvenTileEntity) tileEntity);
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
            World world = inv.player.world;
            BlockPos pos = data.readBlockPos();
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof CookingTableTileEntity) {
                if (((CookingTableTileEntity) tileEntity).hasNoFilterBook()) {
                    return new RecipeBookContainer(windowId, inv.player).setNoFilter().allowCrafting().setKitchenMultiBlock(new KitchenMultiBlock(world, pos));
                } else {
                    return new RecipeBookContainer(windowId, inv.player).allowCrafting().setKitchenMultiBlock(new KitchenMultiBlock(world, pos));
                }
            }

            return null;
        })));

        registry.register(recipeBook = register("no_filter_book", ((windowId, inv, data) -> {
            if (inv.player.getHeldItemMainhand().getItem() == ModItems.craftingBook || inv.player.getHeldItemOffhand().getItem() == ModItems.craftingBook) {
                return new RecipeBookContainer(windowId, inv.player).setNoFilter();
            }

            return null;
        })));

        registry.register(recipeBook = register("recipe_book", ((windowId, inv, data) -> {
            if (inv.player.getHeldItemMainhand().getItem() == ModItems.recipeBook || inv.player.getHeldItemOffhand().getItem() == ModItems.recipeBook) {
                return new RecipeBookContainer(windowId, inv.player);
            }

            return null;
        })));

        registry.register(recipeBook = register("crafting_book", ((windowId, inv, data) -> {
            if (inv.player.getHeldItemMainhand().getItem() == ModItems.noFilterBook || inv.player.getHeldItemOffhand().getItem() == ModItems.noFilterBook) {
                return new RecipeBookContainer(windowId, inv.player).allowCrafting();
            }

            return null;
        })));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerType<T> register(String name, IContainerFactory<T> containerFactory) {
        return (ContainerType<T>) new ContainerType<>(containerFactory).setRegistryName(name);
    }
}
