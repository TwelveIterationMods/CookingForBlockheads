package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.cookingforblockheads.client.gui.screen.RecipeBookScreen;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftableListingFakeSlot;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public class CookingForBlockheadsClient {
    public static void initialize() {
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModTextures.initialize(BalmClient.getTextures());
        ModModels.initialize(BalmClient.getModels());

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (!(Minecraft.getInstance().screen instanceof RecipeBookScreen screen)) {
                return;
            }

            final var player = event.getPlayer();
            if (player == null) {
                return;
            }

            final var menu = screen.getMenu();
            Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
            if (hoverSlot instanceof CraftableListingFakeSlot listingSlot && event.getItemStack() == hoverSlot.getItem()) {
                final var kitchen = menu.getKitchen();
                final var selectedRecipeWithStatus = menu.getSelectedRecipe();
                if (selectedRecipeWithStatus == null) {
                    return;
                }

                final var selectedRecipe = selectedRecipeWithStatus.recipe(player).value();

                if (menu.isSelectedSlot(listingSlot) && kitchen.canProcess(selectedRecipe.getType())) {
                    if (selectedRecipe.getType() == RecipeType.SMELTING) {
                        if (!kitchen.canProcess(RecipeType.SMELTING)) {
                            event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.missing_oven").withStyle(ChatFormatting.RED));
                        } else {
                            if (Screen.hasShiftDown()) {
                                event.getToolTip()
                                        .add(Component.translatable("tooltip.cookingforblockheads.click_to_smelt_stack").withStyle(ChatFormatting.GREEN));
                            } else {
                                event.getToolTip()
                                        .add(Component.translatable("tooltip.cookingforblockheads.click_to_smelt_one").withStyle(ChatFormatting.GREEN));
                            }
                        }
                    } else {
                        final var missingIngredients = selectedRecipeWithStatus.missingIngredients();
                        final var missingAnyUtensils = missingIngredients.stream().anyMatch(it -> isUtensil(it));
                        if (missingAnyUtensils) {
                            event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.missing_tools").withStyle(ChatFormatting.RED));
                        } else if (!missingIngredients.isEmpty()) {
                            event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.missing_ingredients").withStyle(ChatFormatting.RED));
                        } else {
                            if (Screen.hasShiftDown()) {
                                event.getToolTip()
                                        .add(Component.translatable("tooltip.cookingforblockheads.click_to_craft_stack").withStyle(ChatFormatting.GREEN));
                            } else {
                                event.getToolTip()
                                        .add(Component.translatable("tooltip.cookingforblockheads.click_to_craft_one").withStyle(ChatFormatting.GREEN));
                            }
                        }
                    }
                } else {
                    event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.click_to_see_recipe").withStyle(ChatFormatting.YELLOW));
                }
            } else if (hoverSlot instanceof CraftMatrixFakeSlot && event.getItemStack() == hoverSlot.getItem()) {
                if (((CraftMatrixFakeSlot) hoverSlot).getVisibleStacks().size() > 1) {
                    if (((CraftMatrixFakeSlot) hoverSlot).isLocked()) {
                        event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.click_to_unlock").withStyle(ChatFormatting.GREEN));
                    } else {
                        event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.click_to_lock").withStyle(ChatFormatting.GREEN));
                    }
                    event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.scroll_to_switch").withStyle(ChatFormatting.YELLOW));
                }
            }
        });

    }

    private static boolean isUtensil(Ingredient ingredient) {
        for (final var itemStack : ingredient.getItems()) {
            if (itemStack.is(ModItemTags.UTENSILS)) {
                return true;
            }
        }

        return false;
    }
}
