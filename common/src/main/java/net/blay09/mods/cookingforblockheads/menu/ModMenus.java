package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.entity.*;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModMenus {

    private static BalmMenus menus = Balm.getMenus();
    public static DeferredObject<MenuType<CounterMenu>> counter= menus.registerMenu(id("counter"), new BalmMenuFactory<CounterMenu, BlockPos>() {
        @Override
        public CounterMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var tileEntity = inventory.player.level().getBlockEntity(pos);
            return new CounterMenu(windowId, inventory, (CounterBlockEntity) Objects.requireNonNull(tileEntity));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<FridgeMenu>> fridge= menus.registerMenu(id("fridge"), new BalmMenuFactory<FridgeMenu, BlockPos>() {
        @Override
        public FridgeMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var tileEntity = inventory.player.level().getBlockEntity(pos);
            return new FridgeMenu(windowId, inventory, (FridgeBlockEntity) Objects.requireNonNull(tileEntity));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<FruitBasketMenu>> fruitBasket= menus.registerMenu(id("fruit_basket"), new BalmMenuFactory<FruitBasketMenu, BlockPos>() {
        @Override
        public FruitBasketMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var tileEntity = inventory.player.level().getBlockEntity(pos);
            return new FruitBasketMenu(windowId, inventory, (FruitBasketBlockEntity) Objects.requireNonNull(tileEntity));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<OvenMenu>> oven= menus.registerMenu(id("oven"), new BalmMenuFactory<OvenMenu, BlockPos>() {
        @Override
        public OvenMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var tileEntity = inventory.player.level().getBlockEntity(pos);
            return new OvenMenu(windowId, inventory, (OvenBlockEntity) Objects.requireNonNull(tileEntity));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<SpiceRackMenu>> spiceRack= menus.registerMenu(id("spice_rack"), new BalmMenuFactory<SpiceRackMenu, BlockPos>() {
        @Override
        public SpiceRackMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var tileEntity = inventory.player.level().getBlockEntity(pos);
            return new SpiceRackMenu(windowId, inventory, (SpiceRackBlockEntity) Objects.requireNonNull(tileEntity));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<KitchenMenu>> recipeBook= menus.registerMenu(id("recipe_book"), new BalmMenuFactory<KitchenMenu, ItemStack>() {
        @Override
        public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(recipeBook.get(), windowId, inventory.player, kitchen);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
            return ItemStack.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<KitchenMenu>> cookingTable= menus.registerMenu(id("cooking_table"), new BalmMenuFactory<KitchenMenu, BlockPos>() {
        @Override
        public KitchenMenu create(int windowId, Inventory inventory, BlockPos pos) {
            final var level = inventory.player.level();
            final var kitchen = new KitchenImpl(level, pos);
            return new KitchenMenu(cookingTable.get(), windowId, inventory.player, kitchen);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<KitchenMenu>> noFilterBook= menus.registerMenu(id("no_filter_book"), new BalmMenuFactory<KitchenMenu, ItemStack>() {
        @Override
        public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(noFilterBook.get(), windowId, inventory.player, kitchen);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
            return ItemStack.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<KitchenMenu>> craftingBook= menus.registerMenu(id("crafting_book"), new BalmMenuFactory<KitchenMenu, ItemStack>() {
        @Override
        public KitchenMenu create(int windowId, Inventory inventory, ItemStack itemStack) {
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(craftingBook.get(), windowId, inventory.player, kitchen);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStack> getStreamCodec() {
            return ItemStack.STREAM_CODEC.cast();
        }
    });
    public static DeferredObject<MenuType<CuttingBoardMenu>> cuttingBoard= menus.registerMenu(id("cutting_board"), new BalmMenuFactory<CuttingBoardMenu, BlockPos>() {
        @Override
        public CuttingBoardMenu create(int windowId, Inventory inventory, BlockPos pos) {
            return new CuttingBoardMenu(windowId, inventory, ContainerLevelAccess.create(inventory.player.level(), pos));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
            return BlockPos.STREAM_CODEC.cast();
        }
    });

    public static void initialize() {
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, name);
    }
}
