package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.cookingforblockheads.client.gui.screen.KitchenScreen;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftableListingFakeSlot;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashSet;
import java.util.Set;

public class CookingForBlockheadsClient {

    private static final Set<ResourceLocation> favoriteItemIds = new HashSet<>();

    public static void initialize() {
        ModRenderers.initialize(BalmClient.getRenderers());
        ModScreens.initialize(BalmClient.getScreens());
        ModModels.initialize(BalmClient.getModels());

        Balm.getEvents().onEvent(ItemTooltipEvent.class, event -> {
            if (!(Minecraft.getInstance().screen instanceof KitchenScreen screen)) {
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

                if (menu.isSelectedSlot(listingSlot) && kitchen.canProcess(RecipeType.CRAFTING)) {
                    screen.getKitchenFeedback().ifPresentOrElse(component -> event.getToolTip().add(component), () -> {
                        final var processorRecipeType = CookingForBlockheadsRegistry.getProcessorRecipeType(selectedRecipe.getType());
                        if (processorRecipeType.isPresent()) {
                            if (!kitchen.canProcess(selectedRecipe.getType())) {
                                event.getToolTip().add(processorRecipeType.get().missingProcessorComponent().copy().withStyle(ChatFormatting.RED));
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
                            if (selectedRecipeWithStatus.isMissingUtensils()) {
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
                    });
                } else {
                    event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.click_to_see_recipe").withStyle(ChatFormatting.YELLOW));
                }
            } else if (hoverSlot instanceof CraftMatrixFakeSlot matrixSlot && event.getItemStack() == hoverSlot.getItem()) {
                if (matrixSlot.hasMultipleOptions()) {
                    event.getToolTip().add(Component.translatable("tooltip.cookingforblockheads.scroll_to_switch").withStyle(ChatFormatting.YELLOW));
                }
            }
        });

    }

    public static void setFavoriteItems(Set<ResourceLocation> favoriteItemIds) {
        CookingForBlockheadsClient.favoriteItemIds.clear();
        CookingForBlockheadsClient.favoriteItemIds.addAll(favoriteItemIds);
    }

    public static boolean isFavoriteItem(ItemStack itemStack) {
        final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return favoriteItemIds.contains(itemId);
    }
}
