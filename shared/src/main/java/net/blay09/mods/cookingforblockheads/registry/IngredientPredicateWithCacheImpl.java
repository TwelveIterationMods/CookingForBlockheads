package net.blay09.mods.cookingforblockheads.registry;

import net.blay09.mods.cookingforblockheads.api.IngredientPredicateWithCache;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.minecraft.world.item.ItemStack;

public class IngredientPredicateWithCacheImpl implements IngredientPredicateWithCache {

    private final IngredientPredicate predicate;
    private final ItemStack[] items;

    private IngredientPredicateWithCacheImpl(IngredientPredicate predicate, ItemStack... items) {
        this.predicate = predicate;
        this.items = items;
    }

    @Override
    public ItemStack[] getItems() {
        return items;
    }

    @Override
    public boolean test(ItemStack itemStack, int count) {
        return predicate.test(itemStack, count);
    }

    public static IngredientPredicateWithCache of(IngredientPredicate predicate, ItemStack... items) {
        return new IngredientPredicateWithCacheImpl(predicate, items);
    }

    public static IngredientPredicateWithCache and(IngredientPredicate first, IngredientPredicate second) {
        ItemStack[] firstItems = first instanceof IngredientPredicateWithCache ? ((IngredientPredicateWithCache) first).getItems() : new ItemStack[0];
        ItemStack[] secondItems = second instanceof IngredientPredicateWithCache ? ((IngredientPredicateWithCache) second).getItems() : new ItemStack[0];
        ItemStack[] items = new ItemStack[firstItems.length + secondItems.length];
        System.arraycopy(firstItems, 0, items, 0, firstItems.length);
        System.arraycopy(secondItems, 0, items, firstItems.length, secondItems.length);
        return new IngredientPredicateWithCacheImpl(((itemStack, count) -> first.test(itemStack, count) && second.test(itemStack, count)), items);
    }
}
