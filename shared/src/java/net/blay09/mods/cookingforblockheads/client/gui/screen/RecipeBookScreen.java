package net.blay09.mods.cookingforblockheads.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfigData;
import net.blay09.mods.cookingforblockheads.api.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.client.gui.SortButton;
import net.blay09.mods.cookingforblockheads.menu.RecipeBookMenu;
import net.blay09.mods.cookingforblockheads.menu.slot.FakeSlotCraftMatrix;
import net.blay09.mods.cookingforblockheads.menu.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class RecipeBookScreen extends AbstractContainerScreen<RecipeBookMenu> {

    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_Y = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_HEIGHT = 77;

    private static final ResourceLocation guiTexture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/gui.png");
    private static final int VISIBLE_ROWS = 4;

    private final RecipeBookMenu container;
    private int scrollBarScaledHeight;
    private int scrollBarXPos;
    private int scrollBarYPos;
    private int currentOffset;

    private double mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    private Button btnNextRecipe;
    private Button btnPrevRecipe;

    private EditBox searchBar;

    private final List<SortButton> sortButtons = Lists.newArrayList();

    private final String[] noIngredients;
    private final String[] noSelection;

    public RecipeBookScreen(RecipeBookMenu container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.container = container;

        noIngredients = I18n.get("gui.cookingforblockheads:no_ingredients").split("\\\\n");
        noSelection = I18n.get("gui.cookingforblockheads:no_selection").split("\\\\n");
    }

    @Override
    protected void init() {
        imageHeight = 174;
        super.init();

        minecraft.keyboardHandler.setSendRepeatsToGui(true);

        btnPrevRecipe = new Button(width / 2 - 79, height / 2 - 51, 13, 20, new TextComponent("<"), it -> container.nextSubRecipe(-1));
        btnPrevRecipe.visible = false;
        addWidget(btnPrevRecipe);

        btnNextRecipe = new Button(width / 2 - 9, height / 2 - 51, 13, 20, new TextComponent(">"), it -> container.nextSubRecipe(1));
        btnNextRecipe.visible = false;
        addWidget(btnNextRecipe);

        searchBar = new EditBox(minecraft.font, leftPos + imageWidth - 78, topPos - 5, 70, 10, searchBar, new TextComponent(""));
        setInitialFocus(searchBar);

        int yOffset = -80;

        for (ISortButton button : CookingRegistry.getSortButtons()) {
            SortButton sortButton = new SortButton(width / 2 + 87, height / 2 + yOffset, button, it -> {
                container.setSortComparator(((SortButton) it).getComparator(Minecraft.getInstance().player));
            });
            addWidget(sortButton);
            sortButtons.add(sortButton);

            yOffset += 20;
        }

        recalculateScrollBar();
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta == 0) {
            return false;
        }

        if (container.getSelection() != null && mouseX >= leftPos + 7 && mouseY >= topPos + 17 && mouseX < leftPos + 92 && mouseY < topPos + 95) {
            Slot slot = ((AbstractContainerScreenAccessor) this).getHoveredSlot();
            if (slot instanceof FakeSlotCraftMatrix && ((FakeSlotCraftMatrix) slot).getVisibleStacks().size() > 1) {
                ((FakeSlotCraftMatrix) slot).scrollDisplayList(delta > 0 ? -1 : 1);
            }
        } else {
            setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        boolean result = super.mouseReleased(mouseX, mouseY, state);

        if (state != -1 && mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }

        return result;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (button == 1 && mouseX >= searchBar.x && mouseX < searchBar.x + searchBar.getWidth() && mouseY >= searchBar.y && mouseY < searchBar.y + searchBar.getHeight()) {
            searchBar.setValue("");
            container.search(null);
            container.populateRecipeSlots();
            setCurrentOffset(currentOffset);
            return true;
        } else {
            if (searchBar.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
            mouseClickY = mouseY;
            indexWhenClicked = currentOffset;
        }

        Slot mouseSlot = ((AbstractContainerScreenAccessor) this).getHoveredSlot();
        if (mouseSlot instanceof FakeSlotCraftMatrix) {
            if (button == 0) {
                ItemStack itemStack = mouseSlot.getItem();
                FoodRecipeWithStatus recipe = container.findAvailableRecipe(itemStack);
                if (recipe != null) {
                    container.setSelectedRecipe(recipe, false);
                    setCurrentOffset(container.getSelectedRecipeIndex());
                } else if (!CookingRegistry.getFoodRecipes(itemStack).isEmpty()) {
                    container.setSelectedRecipe(new FoodRecipeWithStatus(itemStack, RecipeStatus.MISSING_INGREDIENTS), true);
                }
            } else if (button == 1) {
                ((FakeSlotCraftMatrix) mouseSlot).setLocked(!((FakeSlotCraftMatrix) mouseSlot).isLocked());
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        boolean result = super.charTyped(c, keyCode);

        container.search(searchBar.getValue());
        container.populateRecipeSlots();
        setCurrentOffset(currentOffset);

        return result;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.player.closeContainer();
            return true;
        }

        if (searchBar.keyPressed(keyCode, scanCode, modifiers) || searchBar.isFocused()) {
            container.search(searchBar.getValue());
            container.populateRecipeSlots();
            setCurrentOffset(currentOffset);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        if (container.isDirty()) {
            setCurrentOffset(currentOffset);
            container.setDirty(false);
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, guiTexture);
        blit(poseStack, leftPos, topPos - 10, 0, 0, imageWidth, imageHeight + 10);

        if (mouseClickY != -1) {
            float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(container.getItemListCount() / 3f) - VISIBLE_ROWS);
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        btnPrevRecipe.visible = container.hasVariants();
        btnPrevRecipe.active = container.getSelectionIndex() > 0;
        btnNextRecipe.visible = container.hasVariants();
        btnNextRecipe.active = container.getSelectionIndex() < container.getRecipeCount() - 1;

        boolean hasRecipes = container.getItemListCount() > 0;

        for (Button sortButton : sortButtons) {
            sortButton.active = hasRecipes;
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        Font fontRenderer = minecraft.font;
        FoodRecipeWithIngredients selection = container.getSelection();
        if (selection == null) {
            int curY = topPos + 79 / 2 - noSelection.length / 2 * fontRenderer.lineHeight;
            for (String s : noSelection) {
                fontRenderer.drawShadow(poseStack, s, leftPos + 23 + 27 - fontRenderer.width(s) / 2f, curY, 0xFFFFFFFF);
                curY += fontRenderer.lineHeight + 5;
            }
        } else if (selection.getRecipeType() == FoodRecipeType.SMELTING) {
            blit(poseStack, leftPos + 23, topPos + 19, 54, 184, 54, 54);
        } else {
            blit(poseStack, leftPos + 23, topPos + 19, 0, 184, 54, 54);
        }

        if (selection != null) {
            for (FakeSlotCraftMatrix slot : container.getCraftingMatrixSlots()) {
                if (slot.isLocked() && slot.getVisibleStacks().size() > 1) {
                    blit(poseStack, leftPos + slot.x, topPos + slot.y, 176, 60, 16, 16);
                }
            }
        }

        fill(poseStack, scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        if (container.getItemListCount() == 0) {
            fill(poseStack, leftPos + 97, topPos + 7, leftPos + 168, topPos + 85, 0xAA222222);
            int curY = topPos + 79 / 2 - noIngredients.length / 2 * fontRenderer.lineHeight;
            for (String s : noIngredients) {
                fontRenderer.drawShadow(poseStack, s, leftPos + 97 + 36 - fontRenderer.width(s) / 2f, curY, 0xFFFFFFFF);
                curY += fontRenderer.lineHeight + 5;
            }
        }


        searchBar.renderButton(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, guiTexture);
        if (CookingForBlockheadsConfig.getActive().showIngredientIcon) {
            int prevZLevel = getBlitOffset();
            setBlitOffset(300);
            for (Slot slot : container.slots) {
                if (slot instanceof FakeSlotRecipe) {
                    if (CookingRegistry.isNonFoodRecipe(slot.getItem())) {
                        blit(poseStack, slot.x, slot.y, 176, 76, 16, 16);
                    }

                    FoodRecipeWithStatus recipe = ((FakeSlotRecipe) slot).getRecipe();
                    if (recipe != null && recipe.getStatus() == RecipeStatus.MISSING_TOOLS) {
                        blit(poseStack, slot.x, slot.y, 176, 92, 16, 16);
                    }
                }
            }

            setBlitOffset(prevZLevel);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);

        int prevZLevel = getBlitOffset();
        setBlitOffset(300);
        for (Slot slot : container.slots) {
            if (slot instanceof FakeSlotCraftMatrix) {
                if (!((FakeSlotCraftMatrix) slot).isAvailable() && !slot.getItem().isEmpty()) {
                    fillGradient(poseStack, leftPos + slot.x, topPos + slot.y, leftPos + slot.x + 16, topPos + slot.y + 16, 0x77FF4444, 0x77FF5555);
                }
            }
        }
        setBlitOffset(prevZLevel);

        container.updateSlots(partialTicks);

        for (Button sortButton : this.sortButtons) {
            if (sortButton instanceof SortButton && sortButton.isMouseOver(mouseX, mouseY) && sortButton.active) {
                renderComponentTooltip(poseStack, ((SortButton) sortButton).getTooltipLines(), mouseX, mouseY);
            }
        }

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        super.onClose();

        minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    private void recalculateScrollBar() {
        int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(container.getItemListCount() / 3f)))));
        this.scrollBarXPos = leftPos + imageWidth - SCROLLBAR_WIDTH - 9;
        this.scrollBarYPos = topPos + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((container.getItemListCount() / 3f)) - VISIBLE_ROWS));
    }

    private void setCurrentOffset(int currentOffset) {
        this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getItemListCount() / 3f) - VISIBLE_ROWS));

        container.setScrollOffset(this.currentOffset);

        recalculateScrollBar();
    }

    /* TODO public void onItemTooltip(ItemTooltipEvent event) {
        Slot hoverSlot = getSlotUnderMouse();
        if (hoverSlot instanceof FakeSlotRecipe && event.getItemStack() == hoverSlot.getStack()) {
            FakeSlotRecipe slotRecipe = (FakeSlotRecipe) hoverSlot;
            if (container.isSelectedSlot(slotRecipe) && container.isAllowCrafting()) {
                FoodRecipeWithIngredients subRecipe = container.getSelection();
                if (subRecipe == null) {
                    return;
                }

                if (subRecipe.getRecipeType() == FoodRecipeType.SMELTING) {
                    if (!container.hasOven()) {
                        event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:missing_oven", ChatFormatting.RED));
                    } else {
                        if (hasShiftDown()) {
                            event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_smelt_stack", ChatFormatting.GREEN));
                        } else {
                            event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_smelt_one", ChatFormatting.GREEN));
                        }
                    }
                } else {
                    if (subRecipe.getRecipeStatus() == RecipeStatus.MISSING_TOOLS) {
                        event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:missing_tools", ChatFormatting.RED));
                    } else if (subRecipe.getRecipeStatus() == RecipeStatus.MISSING_INGREDIENTS) {
                        event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:missing_ingredients", ChatFormatting.RED));
                    } else {
                        if (hasShiftDown()) {
                            event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_craft_stack", ChatFormatting.GREEN));
                        } else {
                            event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_craft_one", ChatFormatting.GREEN));
                        }
                    }
                }
            } else {
                event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_see_recipe", ChatFormatting.YELLOW));
            }
        } else if (hoverSlot instanceof FakeSlotCraftMatrix && event.getItemStack() == hoverSlot.getStack()) {
            if (((FakeSlotCraftMatrix) hoverSlot).getVisibleStacks().size() > 1) {
                if (((FakeSlotCraftMatrix) hoverSlot).isLocked()) {
                    event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_unlock", ChatFormatting.GREEN));
                } else {
                    event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:click_to_lock", ChatFormatting.GREEN));
                }
                event.getToolTip().add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:scroll_to_switch", ChatFormatting.YELLOW));
            }
        }
    }*/

    public Button[] getSortingButtons() {
        return sortButtons.toArray(new SortButton[0]);
    }

}
