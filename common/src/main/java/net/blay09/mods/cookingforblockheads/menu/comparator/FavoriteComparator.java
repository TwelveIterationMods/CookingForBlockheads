package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.crafting.CraftableWithStatus;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;

import java.util.Comparator;

public class FavoriteComparator implements Comparator<CraftableWithStatus> {
    private final Comparator<CraftableWithStatus> fallback;

    public FavoriteComparator(Comparator<CraftableWithStatus> fallback) {
        this.fallback = fallback;
    }

    @Override
    public int compare(CraftableWithStatus o1, CraftableWithStatus o2) {
        final var isFavorite = CookingForBlockheadsClient.isFavoriteItem(o1.itemStack());
        final var isFavorite2 = CookingForBlockheadsClient.isFavoriteItem(o2.itemStack());
        if (isFavorite && !isFavorite2) {
            return -1;
        } else if (!isFavorite && isFavorite2) {
            return 1;
        }

        return fallback.compare(o1, o2);
    }
}
