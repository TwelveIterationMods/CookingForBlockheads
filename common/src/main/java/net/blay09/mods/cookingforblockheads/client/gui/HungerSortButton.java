package net.blay09.mods.cookingforblockheads.client.gui;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.crafting.CraftableWithStatus;
import net.blay09.mods.cookingforblockheads.menu.comparator.ComparatorHunger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class HungerSortButton implements ISortButton {

    public static final Identifier ID = id("hunger");
    private static final Identifier icon = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "textures/gui/gui.png");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Identifier getIcon() {
        return icon;
    }

    @Override
    public Component getTooltip() {
        return Component.translatable("tooltip.cookingforblockheads.sort_by_hunger");
    }

    @Override
    public Comparator<CraftableWithStatus> getComparator(Player player) {
        return new ComparatorHunger(player);
    }

    @Override
    public int getIconTextureX() {
        return 216;
    }

    @Override
    public int getIconTextureY() {
        return 0;
    }

}
