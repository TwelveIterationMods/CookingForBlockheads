package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;

import java.util.Comparator;

public class FavoriteComparator implements Comparator<FoodRecipeWithStatus> {
    private final Comparator<FoodRecipeWithStatus> fallback;

    public FavoriteComparator(Comparator<FoodRecipeWithStatus> fallback) {
        this.fallback = fallback;
    }

    @Override
    public int compare(FoodRecipeWithStatus o1, FoodRecipeWithStatus o2) {
        final var isFavorite = CookingForBlockheadsClient.isFavoriteItem(o1.getOutputItem());
        final var isFavorite2 = CookingForBlockheadsClient.isFavoriteItem(o2.getOutputItem());
        if (isFavorite && !isFavorite2) {
            return -1;
        } else if (!isFavorite && isFavorite2) {
            return 1;
        }

        return fallback.compare(o1, o2);
    }
}
