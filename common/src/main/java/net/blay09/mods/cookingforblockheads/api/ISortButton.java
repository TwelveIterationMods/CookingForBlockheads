package net.blay09.mods.cookingforblockheads.api;

import net.blay09.mods.cookingforblockheads.crafting.CraftableWithStatus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.Locale;

public interface ISortButton {
    default Identifier getId() {
        final var path = "sort/" + getClass().getName()
                .toLowerCase(Locale.ROOT)
                .replace('.', '/')
                .replace('$', '/')
                .replaceAll("[^a-z0-9_/.-]", "_");
        return Identifier.fromNamespaceAndPath("cookingforblockheads", path);
    }

    Identifier getIcon();

    Component getTooltip();

    Comparator<CraftableWithStatus> getComparator(Player player);

    int getIconTextureX();

    int getIconTextureY();
}
