package net.blay09.mods.cookingforblockheads.compat;

import com.cazsius.solcarrot.api.SOLCarrotAPI;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.menu.comparator.ComparatorName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;

public class SpiceOfLifeAddon {
    public SpiceOfLifeAddon() {
        CookingForBlockheadsAPI.addSortButton(new ISortButton() {

            private final ComparatorName fallback = new ComparatorName();
            private static final ResourceLocation icon = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/gui.png");

            @Override
            public ResourceLocation getIcon() {
                return icon;
            }

            @Override
            public String getTooltip() {
                return "tooltip.cookingforblockheads:sort_by_eatenness";
            }

            @Override
            public Comparator<FoodRecipeWithStatus> getComparator(Player player) {
                return (o1, o2) -> {
                    final var foodCapability = SOLCarrotAPI.getFoodCapability(player);
                    final var isEdibleO1 = o1.getOutputItem().isEdible();
                    final var isEdibleO2 = o2.getOutputItem().isEdible();
                    final var hasEatenO1 = foodCapability.hasEaten(o1.getOutputItem().getItem());
                    final var hasEatenO2 = foodCapability.hasEaten(o2.getOutputItem().getItem());

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
