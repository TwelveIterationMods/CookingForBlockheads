package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.KitchenMultiBlock;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModMenus {

    public static DeferredObject<MenuType<CounterMenu>> counter;
    public static DeferredObject<MenuType<FridgeMenu>> fridge;
    public static DeferredObject<MenuType<FruitBasketMenu>> fruitBasket;
    public static DeferredObject<MenuType<OvenMenu>> oven;
    public static DeferredObject<MenuType<SpiceRackMenu>> spiceRack;
    public static DeferredObject<MenuType<RecipeBookMenu>> recipeBook;
    public static DeferredObject<MenuType<RecipeBookMenu>> cookingTable;
    public static DeferredObject<MenuType<RecipeBookMenu>> noFilterBook;
    public static DeferredObject<MenuType<RecipeBookMenu>> craftingBook;

    public static void initialize(BalmMenus menus) {
        counter = menus.registerMenu(id("counter"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level.getBlockEntity(pos);
            return new CounterMenu(windowId, inv, (CounterBlockEntity) Objects.requireNonNull(tileEntity));
        });

        fridge = menus.registerMenu(id("fridge"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level.getBlockEntity(pos);
            return new FridgeMenu(windowId, inv, (FridgeBlockEntity) Objects.requireNonNull(tileEntity));
        });

        fruitBasket = menus.registerMenu(id("fruit_basket"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level.getBlockEntity(pos);
            return new FruitBasketMenu(windowId, inv, (FruitBasketBlockEntity) Objects.requireNonNull(tileEntity));
        });

        oven = menus.registerMenu(id("oven"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level.getBlockEntity(pos);
            return new OvenMenu(windowId, inv, (OvenBlockEntity) Objects.requireNonNull(tileEntity));
        });

        spiceRack = menus.registerMenu(id("spice_rack"), (windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = inv.player.level.getBlockEntity(pos);
            return new SpiceRackMenu(windowId, inv, (SpiceRackBlockEntity) Objects.requireNonNull(tileEntity));
        });

        cookingTable = menus.registerMenu(id("cooking_table"), (windowId, inv, data) -> {
            Level level = inv.player.level;
            BlockPos pos = data.readBlockPos();
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (((CookingTableBlockEntity) Objects.requireNonNull(tileEntity)).hasNoFilterBook()) {
                return new RecipeBookMenu(cookingTable.get(), windowId, inv.player).setNoFilter().allowCrafting().setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(level, pos));
            }
            return new RecipeBookMenu(cookingTable.get(), windowId, inv.player).allowCrafting().setKitchenMultiBlock(KitchenMultiBlock.buildFromLocation(level, pos));
        });

        noFilterBook = menus.registerMenu(id("no_filter_book"), ((windowId, inv, data) -> new RecipeBookMenu(noFilterBook.get(), windowId, inv.player).setNoFilter()));
        recipeBook = menus.registerMenu(id("recipe_book"), ((windowId, inv, data) -> new RecipeBookMenu(recipeBook.get(), windowId, inv.player)));
        craftingBook = menus.registerMenu(id("crafting_book"), ((windowId, inv, data) -> new RecipeBookMenu(craftingBook.get(), windowId, inv.player).allowCrafting()));
    }

    @NotNull
    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }
}
