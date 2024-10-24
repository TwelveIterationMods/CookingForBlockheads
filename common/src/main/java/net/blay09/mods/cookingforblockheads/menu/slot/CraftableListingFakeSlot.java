package net.blay09.mods.cookingforblockheads.menu.slot;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jetbrains.annotations.Nullable;

public class CraftableListingFakeSlot extends AbstractFakeSlot {

    private final KitchenMenu menu;
    private RecipeWithStatus craftable;

    public CraftableListingFakeSlot(KitchenMenu menu, Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
        this.menu = menu;
    }

    @Override
    public ItemStack getItem() {
        if (craftable != null) {
            final var contextMap = SlotDisplayContext.fromLevel(menu.player.level());
            return craftable.recipeDisplayEntry().display().result().resolveForFirstStack(contextMap);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return craftable != null;
    }

    @Override
    public boolean isActive() {
        return craftable != null;
    }

    public void setCraftable(@Nullable RecipeWithStatus craftable) {
        this.craftable = craftable;
    }

    @Nullable
    public RecipeWithStatus getCraftable() {
        return craftable;
    }

    @Override
    public boolean isFake() {
        return true;
    }
}

