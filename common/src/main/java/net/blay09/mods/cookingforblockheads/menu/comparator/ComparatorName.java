package net.blay09.mods.cookingforblockheads.menu.comparator;

import net.blay09.mods.cookingforblockheads.crafting.RecipeWithStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.Comparator;

public class ComparatorName implements Comparator<RecipeWithStatus> {

    private final Player player;

    public ComparatorName(Player player) {
        this.player = player;
    }

    @Override
    public int compare(RecipeWithStatus o1, RecipeWithStatus o2) {
        final var contextMap = SlotDisplayContext.fromLevel(player.level());
        final var firstItem = o1.recipeDisplayEntry().display().result().resolveForFirstStack(contextMap);
        final var secondItem = o2.recipeDisplayEntry().display().result().resolveForFirstStack(contextMap);
        String s1 = firstItem.getDisplayName().getString();
        String s2 = secondItem.getDisplayName().getString();
        return s1.compareToIgnoreCase(s2);
    }

}
