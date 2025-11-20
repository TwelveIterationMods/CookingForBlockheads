package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.cookingforblockheads.block.entity.*;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ModMenus {

    public static Holder<MenuType<CounterMenu>> counter;
    public static Holder<MenuType<FridgeMenu>> fridge;
    public static Holder<MenuType<FruitBasketMenu>> fruitBasket;
    public static Holder<MenuType<OvenMenu>> oven;
    public static Holder<MenuType<SpiceRackMenu>> spiceRack;
    public static Holder<MenuType<KitchenMenu>> recipeBook;
    public static Holder<MenuType<KitchenMenu>> cookingTable;
    public static Holder<MenuType<KitchenMenu>> noFilterBook;
    public static Holder<MenuType<KitchenMenu>> craftingBook;
    public static Holder<MenuType<CuttingBoardMenu>> cuttingBoard;

    public static void initialize(BalmMenuTypeRegistrar menuTypes) {
        counter = menuTypes.register("counter", new BalmMenuFactory<CounterMenu, BlockPos>() {
            @Override
            public CounterMenu create(int syncId, Inventory inventory, BlockPos pos) {
                final var tileEntity = inventory.player.level().getBlockEntity(pos);
                return new CounterMenu(syncId, inventory, (CounterBlockEntity) Objects.requireNonNull(tileEntity));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();

        fridge = menuTypes.register("fridge", new BalmMenuFactory<FridgeMenu, BlockPos>() {
            @Override
            public FridgeMenu create(int windowId, Inventory inventory, BlockPos pos) {
                final var tileEntity = inventory.player.level().getBlockEntity(pos);
                return new FridgeMenu(windowId, inventory, (FridgeBlockEntity) Objects.requireNonNull(tileEntity));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
        fruitBasket = menuTypes.register("fruit_basket", new BalmMenuFactory<FruitBasketMenu, BlockPos>() {
            @Override
            public FruitBasketMenu create(int windowId, Inventory inventory, BlockPos pos) {
                final var tileEntity = inventory.player.level().getBlockEntity(pos);
                return new FruitBasketMenu(windowId, inventory, (FruitBasketBlockEntity) Objects.requireNonNull(tileEntity));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
        oven = menuTypes.register("oven", new BalmMenuFactory<OvenMenu, BlockPos>() {
            @Override
            public OvenMenu create(int windowId, Inventory inventory, BlockPos pos) {
                final var tileEntity = inventory.player.level().getBlockEntity(pos);
                return new OvenMenu(windowId, inventory, (OvenBlockEntity) Objects.requireNonNull(tileEntity));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
        spiceRack = menuTypes.register("spice_rack", new BalmMenuFactory<SpiceRackMenu, BlockPos>() {
            @Override
            public SpiceRackMenu create(int windowId, Inventory inventory, BlockPos pos) {
                final var tileEntity = inventory.player.level().getBlockEntity(pos);
                return new SpiceRackMenu(windowId, inventory, (SpiceRackBlockEntity) Objects.requireNonNull(tileEntity));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
        recipeBook = menuTypes.register("recipe_book", new BalmMenuFactory<KitchenMenu, ItemStack>() {
            @Override
            public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
                final var kitchen = new KitchenImpl(itemStack);
                return new KitchenMenu(recipeBook.value(), windowId, inventory.player, kitchen);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
                return ItemStack.STREAM_CODEC.cast();
            }
        }).asHolder();
        cookingTable = menuTypes.register("cooking_table", new BalmMenuFactory<KitchenMenu, BlockPos>() {
            @Override
            public KitchenMenu create(int windowId, Inventory inventory, BlockPos pos) {
                final var level = inventory.player.level();
                final var kitchen = new KitchenImpl(level, pos);
                return new KitchenMenu(cookingTable.value(), windowId, inventory.player, kitchen);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
        noFilterBook = menuTypes.register("no_filter_book", new BalmMenuFactory<KitchenMenu, ItemStack>() {
            @Override
            public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
                final var kitchen = new KitchenImpl(itemStack);
                return new KitchenMenu(noFilterBook.value(), windowId, inventory.player, kitchen);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
                return ItemStack.STREAM_CODEC.cast();
            }
        }).asHolder();
        craftingBook = menuTypes.register("crafting_book", new BalmMenuFactory<KitchenMenu, ItemStack>() {
            @Override
            public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
                final var kitchen = new KitchenImpl(itemStack);
                return new KitchenMenu(craftingBook.value(), windowId, inventory.player, kitchen);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
                return ItemStack.STREAM_CODEC.cast();
            }
        }).asHolder();

        cuttingBoard = menuTypes.register("cutting_board", new BalmMenuFactory<CuttingBoardMenu, BlockPos>() {
            @Override
            public CuttingBoardMenu create(int windowId, Inventory inventory, BlockPos pos) {
                return new CuttingBoardMenu(windowId, inventory, ContainerLevelAccess.create(inventory.player.level(), pos));
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
    }


}
