package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;

import java.util.Comparator;

public class FavoriteComparator implements Comparator<RecipeWithStatus> {
    private final Comparator<RecipeWithStatus> fallback;

    public FavoriteComparator(Comparator<RecipeWithStatus> fallback) {
        this.fallback = fallback;
    }

    @Override
    public int compare(RecipeWithStatus o1, RecipeWithStatus o2) {
        final var isFavorite = CookingForBlockheadsClient.isFavoriteItem(o1.resultItem());
        final var isFavorite2 = CookingForBlockheadsClient.isFavoriteItem(o2.resultItem());
        if (isFavorite && !isFavorite2) {
            return -1;
        } else if (!isFavorite && isFavorite2) {
            return 1;
        }

        return fallback.compare(o1, o2);
    }
}
