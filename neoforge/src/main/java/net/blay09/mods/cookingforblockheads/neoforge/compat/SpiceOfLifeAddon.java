package net.blay09.mods.cookingforblockheads.neoforge.compat;

import com.cazsius.solcarrot.api.SOLCarrotAPI;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.crafting.CraftableWithStatus;
import net.blay09.mods.cookingforblockheads.menu.comparator.ComparatorName;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class SpiceOfLifeAddon {
    public SpiceOfLifeAddon() {
        CookingForBlockheadsAPI.addSortButton(new ISortButton() {

            private final ComparatorName fallback = new ComparatorName();
            private static final Identifier ID = id("spice_of_life/eatenness");
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
                return Component.translatable("tooltip.cookingforblockheads.sort_by_eatenness");
            }

            @Override
            public Comparator<CraftableWithStatus> getComparator(Player player) {
                return (o1, o2) -> {
                    final var foodCapability = SOLCarrotAPI.getFoodCapability(player);
                    final var isEdibleO1 = o1.itemStack().has(DataComponents.FOOD);
                    final var isEdibleO2 = o2.itemStack().has(DataComponents.FOOD);
                    final var hasEatenO1 = foodCapability.hasEaten(o1.itemStack().getItem());
                    final var hasEatenO2 = foodCapability.hasEaten(o2.itemStack().getItem());

                    if (isEdibleO1 && !isEdibleO2) {
                        return -1;
                    } else if (!isEdibleO1 && isEdibleO2) {
                        return 1;
                    }

                    //noinspection ConstantValue
                    if (isEdibleO1 && isEdibleO2) {
                        if (!hasEatenO1 && hasEatenO2) {
                            return -1;
                        } else if (hasEatenO1 && !hasEatenO2) {
                            return 1;
                        }
                    }

                    return fallback.compare(o1, o2);
                };
            }

            @Override
            public int getIconTextureX() {
                return 236;
            }

            @Override
            public int getIconTextureY() {
                return 60;
            }
        });
    }
}
