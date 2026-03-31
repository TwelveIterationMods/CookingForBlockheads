package net.blay09.mods.cookingforblockheads.menu.slot;

import net.blay09.mods.cookingforblockheads.crafting.IngredientAmount;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class CraftMatrixFakeSlot extends AbstractFakeSlot {

    private static final float ITEM_SWITCH_TIME = 40f;

    private final NonNullList<ItemStack> visibleStacks = NonNullList.create();
    private final KitchenMenu menu;
    private List<IngredientAmount> ingredientAmounts = List.of();

    private int ingredientIndex;
    private @Nullable SlotDisplay slotDisplay;
    private float variantTimePassed;
    private int currentVariantIndex;
    private boolean isLocked;
    private boolean missing = true;

    public CraftMatrixFakeSlot(KitchenMenu menu, Container container, int slotId, int x, int y) {
        super(container, slotId, x, y);
        this.menu = menu;
    }

    public void setIngredient(final int ingredientIndex, @Nullable SlotDisplay slotDisplay, final ItemStack lockedInput) {
        this.ingredientIndex = ingredientIndex;

        final var previousIngredient = this.slotDisplay;
        var effectiveLockedInput = isLocked ? getItem() : ItemStack.EMPTY;
        if (!lockedInput.isEmpty()) {
            effectiveLockedInput = lockedInput;
        }
        visibleStacks.clear();
        this.slotDisplay = slotDisplay;
        if (slotDisplay != null) {
            final var itemStacks = slotDisplay.resolveForStacks(SlotDisplayContext.fromLevel(menu.player.level()));
            for (final var itemStack : itemStacks) {
                if (!itemStack.isEmpty()) {
                    visibleStacks.add(itemStack.copyWithCount(1));
                }
            }
        }
        visibleStacks.sort(Comparator.comparing(it -> BuiltInRegistries.ITEM.getKey(it.getItem()).toString()));

        variantTimePassed = 0;
        if (previousIngredient != slotDisplay) {
            currentVariantIndex = 0;
        } else {
            currentVariantIndex = !visibleStacks.isEmpty() ? currentVariantIndex % visibleStacks.size() : 0;
        }
        isLocked = false;

        if (!effectiveLockedInput.isEmpty()) {
            for (int i = 0; i < visibleStacks.size(); i++) {
                if (ItemStack.isSameItemSameComponents(visibleStacks.get(i), effectiveLockedInput)) {
                    currentVariantIndex = i;
                    isLocked = true;
                }
            }
        }
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }

    public void setIngredientAmounts(List<IngredientAmount> ingredientAmounts) {
        this.ingredientAmounts = ingredientAmounts;
    }

    public boolean isMissing() {
        return missing;
    }

    public void updateSlot(float partialTicks) {
        if (!isLocked) {
            variantTimePassed += partialTicks;
            if (variantTimePassed >= ITEM_SWITCH_TIME) {
                currentVariantIndex++;
                if (currentVariantIndex >= visibleStacks.size()) {
                    currentVariantIndex = 0;
                }
                variantTimePassed = 0;
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return !visibleStacks.isEmpty() ? visibleStacks.get(currentVariantIndex) : ItemStack.EMPTY;
    }

    @Override
    public boolean hasItem() {
        return !visibleStacks.isEmpty();
    }

    @Override
    public boolean isActive() {
        return !visibleStacks.isEmpty();
    }

    public NonNullList<ItemStack> getVisibleStacks() {
        return visibleStacks;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public ItemStack scrollDisplayListAndLock(int i) {
        isLocked = true;
        currentVariantIndex += i;
        if (currentVariantIndex >= visibleStacks.size()) {
            currentVariantIndex = 0;
        } else if (currentVariantIndex < 0) {
            currentVariantIndex = visibleStacks.size() - 1;
        }
        return visibleStacks.get(currentVariantIndex);
    }

    public ItemStack toggleLock() {
        isLocked = !isLocked;
        return isLocked ? getItem() : ItemStack.EMPTY;
    }

    public int getIngredientIndex() {
        return ingredientIndex;
    }

    public int getDisplayedAmount() {
        final var displayedStack = getItem();
        if (displayedStack.isEmpty()) {
            return 0;
        }

        return ingredientAmounts.stream()
                .filter(it -> ItemStack.isSameItemSameComponents(it.itemStack(), displayedStack))
                .mapToInt(IngredientAmount::amount)
                .findFirst()
                .orElse(0);
    }
}
