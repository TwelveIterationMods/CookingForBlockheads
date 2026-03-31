package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.balm.platform.event.callback.ItemCallback;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.screen.KitchenScreen;
import net.blay09.mods.cookingforblockheads.compat.recipeviewers.CowJarRecipe;
import net.blay09.mods.cookingforblockheads.crafting.CraftingContext;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftableListingFakeSlot;
import net.blay09.mods.kuma.api.Kuma;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class CookingForBlockheadsClient {

    private static final Set<Identifier> favoriteItemIds = new HashSet<>();

    public static void initialize(BalmClientRegistrars registrars) {
        registrars.blockEntityRenderers(ModRenderers::initialize);
        registrars.menuScreens(ModMenuScreens::initialize);
        registrars.blockColors(ModRenderers::initialize);
        registrars.blockStateModels(ModModels::initialize);

        Balm.modSupport().recipeViewers().register(id("recipes"), registrar -> {
            registrar.registerScreenOcclusion(KitchenScreen.class, containerScreen -> containerScreen.getSortingButtons().stream()
                    .map(button -> new Rect2i(button.getX(), button.getY(), button.getWidth(), button.getHeight()))
                    .toList());

            if (CookingForBlockheadsConfig.getActive().cowJarEnabled) {
                registrar.registerCustomRecipeType(id("cow_jar"), CowJarRecipe.class)
                        .withCraftingStation(ModBlocks.cowJar)
                        .withRecipe(new CowJarRecipe())
                        .buildDisplay(display -> display
                                .title(Component.translatable("container.cookingforblockheads.cow_jar"))
                                .icon(ModBlocks.cowJar)
                                .background(id("textures/gui/jei_cow_jar.png"))
                                .size(150, 110)
                                .slots((_, slots) -> {
                                    slots.inputSlot(65, 1).add(Items.ANVIL);
                                    slots.craftingStationSlot(65, 77).withSlotBackground().add(ModBlocks.milkJar);
                                    slots.outputSlot(123, 77).withSlotBackground().add(ModBlocks.cowJar);
                                })
                        );
            }
        });

        ItemCallback.Tooltip.EVENT.register((itemStack, tooltip, _) -> {
            if (!(Minecraft.getInstance().screen instanceof KitchenScreen screen)) {
                return;
            }

            final var menu = screen.getMenu();
            Slot hoverSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();
            if (hoverSlot instanceof CraftableListingFakeSlot listingSlot && itemStack == hoverSlot.getItem()) {
                final var kitchen = menu.getKitchen();
                final var selectedRecipeWithStatus = menu.getSelectedRecipe();
                if (selectedRecipeWithStatus == null) {
                    return;
                }

                final var selectedRecipeDisplay = selectedRecipeWithStatus.recipeDisplayEntry().display();
                final var canActivateSelectedRecipe = switch (selectedRecipeDisplay) {
                    case FurnaceRecipeDisplay _ -> kitchen.canProcess(RecipeType.SMELTING);
                    case ShapedCraftingRecipeDisplay _ -> kitchen.canProcess(RecipeType.CRAFTING);
                    case ShapelessCraftingRecipeDisplay shapelessCraftingRecipeDisplay -> shapelessCraftingRecipeDisplay.ingredients().isEmpty() || kitchen.canProcess(RecipeType.CRAFTING);
                    default -> true;
                };

                if (menu.isSelectedSlot(listingSlot) && canActivateSelectedRecipe) {
                    if (selectedRecipeDisplay instanceof FurnaceRecipeDisplay) {
                        if (Kuma.hasShiftDown()) {
                            tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_smelt_stack").withStyle(ChatFormatting.GREEN));
                        } else {
                            tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_smelt_one").withStyle(ChatFormatting.GREEN));
                        }
                    } else {
                        final var missingIngredients = selectedRecipeWithStatus.missingIngredients();
                        if (selectedRecipeWithStatus.isMissingUtensils()) {
                            tooltip.add(Component.translatable("tooltip.cookingforblockheads.missing_tools").withStyle(ChatFormatting.RED));
                        } else if (!missingIngredients.isEmpty()) {
                            tooltip.add(Component.translatable("tooltip.cookingforblockheads.missing_ingredients").withStyle(ChatFormatting.RED));
                        } else {
                            if (Kuma.hasShiftDown()) {
                                tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_craft_stack").withStyle(ChatFormatting.GREEN));
                            } else {
                                tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_craft_one").withStyle(ChatFormatting.GREEN));
                            }
                        }
                    }
                } else if (menu.isSelectedSlot(listingSlot) && selectedRecipeDisplay instanceof FurnaceRecipeDisplay) {
                    tooltip.add(Component.translatable("tooltip.cookingforblockheads.missing_oven").withStyle(ChatFormatting.RED));
                } else {
                    tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_see_recipe").withStyle(ChatFormatting.YELLOW));
                }
            } else if (hoverSlot instanceof CraftMatrixFakeSlot && itemStack == hoverSlot.getItem()) {
                tooltip.add(Component.translatable("tooltip.cookingforblockheads.available_ingredients", formatCount(((CraftMatrixFakeSlot) hoverSlot).getDisplayedAmount()))
                        .withStyle(ChatFormatting.GRAY));
                if (((CraftMatrixFakeSlot) hoverSlot).getVisibleStacks().size() > 1) {
                    if (((CraftMatrixFakeSlot) hoverSlot).isLocked()) {
                        tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_unlock").withStyle(ChatFormatting.GREEN));
                    } else {
                        tooltip.add(Component.translatable("tooltip.cookingforblockheads.click_to_lock").withStyle(ChatFormatting.GREEN));
                    }
                    tooltip.add(Component.translatable("tooltip.cookingforblockheads.scroll_to_switch").withStyle(ChatFormatting.YELLOW));
                }
            }
        });

    }

    public static void setFavoriteItems(Set<Identifier> favoriteItemIds) {
        CookingForBlockheadsClient.favoriteItemIds.clear();
        CookingForBlockheadsClient.favoriteItemIds.addAll(favoriteItemIds);
    }

    public static boolean isFavoriteItem(ItemStack itemStack) {
        final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return favoriteItemIds.contains(itemId);
    }

    public static String formatCount(int amount) {
        return amount >= CraftingContext.COUNT_CUTOFF ? CraftingContext.COUNT_CUTOFF + "+" : Integer.toString(amount);
    }
}
