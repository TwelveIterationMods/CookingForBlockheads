package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.entity.*;
import net.blay09.mods.cookingforblockheads.crafting.KitchenImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModMenus {

    public static DeferredObject<MenuType<CounterMenu>> counter;
    public static DeferredObject<MenuType<FridgeMenu>> fridge;
    public static DeferredObject<MenuType<FruitBasketMenu>> fruitBasket;
    public static DeferredObject<MenuType<OvenMenu>> oven;
    public static DeferredObject<MenuType<SpiceRackMenu>> spiceRack;
    public static DeferredObject<MenuType<KitchenMenu>> recipeBook;
    public static DeferredObject<MenuType<KitchenMenu>> cookingTable;
    public static DeferredObject<MenuType<KitchenMenu>> noFilterBook;
    public static DeferredObject<MenuType<KitchenMenu>> craftingBook;

    public static void initialize(BalmMenus menus) {
        counter = menus.registerMenu(id("counter"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level().getBlockEntity(pos);
            return new CounterMenu(windowId, inv, (CounterBlockEntity) Objects.requireNonNull(tileEntity));
        });

        fridge = menus.registerMenu(id("fridge"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level().getBlockEntity(pos);
            return new FridgeMenu(windowId, inv, (FridgeBlockEntity) Objects.requireNonNull(tileEntity));
        });

        fruitBasket = menus.registerMenu(id("fruit_basket"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level().getBlockEntity(pos);
            return new FruitBasketMenu(windowId, inv, (FruitBasketBlockEntity) Objects.requireNonNull(tileEntity));
        });

        oven = menus.registerMenu(id("oven"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level().getBlockEntity(pos);
            return new OvenMenu(windowId, inv, (OvenBlockEntity) Objects.requireNonNull(tileEntity));
        });

        spiceRack = menus.registerMenu(id("spice_rack"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level().getBlockEntity(pos);
            return new SpiceRackMenu(windowId, inv, (SpiceRackBlockEntity) Objects.requireNonNull(tileEntity));
        });

        cookingTable = menus.registerMenu(id("cooking_table"), (windowId, inv, data) -> {
            final var level = inv.player.level();
            final var pos = data.readBlockPos();
            final var kitchen = new KitchenImpl(level, pos);
            return new KitchenMenu(cookingTable.get(), windowId, inv.player, kitchen);
        });

        noFilterBook = menus.registerMenu(id("no_filter_book"), ((windowId, inv, data) -> {
            final var itemStack = data.readItem();
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(noFilterBook.get(), windowId, inv.player, kitchen);
        }));
        recipeBook = menus.registerMenu(id("recipe_book"), ((windowId, inv, data) -> {
            final var itemStack = data.readItem();
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(recipeBook.get(), windowId, inv.player, kitchen);
        }));
        craftingBook = menus.registerMenu(id("crafting_book"), ((windowId, inv, data) -> {
            final var itemStack = data.readItem();
            final var kitchen = new KitchenImpl(itemStack);
            return new KitchenMenu(craftingBook.get(), windowId, inv.player, kitchen);
        }));
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }
}
