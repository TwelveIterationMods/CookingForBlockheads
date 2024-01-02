package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.client.gui.screen.RecipeBookScreen;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.RecipeFakeSlot;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeType;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

public class CookingForBlockheadsClient {
    public static void initialize() {
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModTextures.initialize(BalmClient.getTextures());
        ModModels.initialize(BalmClient.getModels());

        Balm.getEvents().onEvent(RecipesUpdatedEvent.class, event -> {
            CookingRegistry.initFoodRegistry(event.getRecipeManager(), event.getRegistryAccess());
        });

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (!(Minecraft.getInstance().screen instanceof RecipeBookScreen screen)) {
                return;
            }

            RecipeBookMenu menu = screen.getMenu();
            Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
            if (hoverSlot instanceof RecipeFakeSlot recipeSlot && event.getItemStack() == hoverSlot.getItem()) {
                if (menu.isSelectedSlot(recipeSlot) && menu.isAllowCrafting()) {
                    FoodRecipeWithIngredients subRecipe = menu.getSelection();
                    if (subRecipe == null) {
                        return;
                    }

                    if (subRecipe.getRecipeType() == FoodRecipeType.SMELTING) {
                        if (!menu.hasOven()) {
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
                        if (subRecipe.getRecipeStatus() == RecipeStatus.MISSING_TOOLS) {
                            event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.missing_tools").withStyle(ChatFormatting.RED));
                        } else if (subRecipe.getRecipeStatus() == RecipeStatus.MISSING_INGREDIENTS) {
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
}
