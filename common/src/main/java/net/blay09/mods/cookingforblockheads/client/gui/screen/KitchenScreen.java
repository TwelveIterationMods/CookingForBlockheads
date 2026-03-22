package net.blay09.mods.cookingforblockheads.client.gui.screen;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.client.gui.SortButton;
import net.blay09.mods.cookingforblockheads.menu.KitchenMenu;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftMatrixFakeSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.CraftableListingFakeSlot;
import net.blay09.mods.cookingforblockheads.registry.CookingForBlockheadsRegistry;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.blay09.mods.cookingforblockheads.network.message.ToggleFavoriteMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KitchenScreen extends AbstractContainerScreen<KitchenMenu> {

    private static final float KITCHEN_FEEDBACK_HINT_TIME = 40f;
    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_Y = 8;
    private static final int SCROLLBAR_WIDTH = 7;
    private static final int SCROLLBAR_HEIGHT = 77;

    private static final Identifier guiTexture = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "textures/gui/gui.png");
    private static final int VISIBLE_ROWS = 4;
    private static final int VISIBLE_COLS = 3;

    private int scrollBarScaledHeight;
    private int scrollBarXPos;
    private int scrollBarYPos;
    private int currentOffset;

    private Component kitchenFeedback;
    private float kitchenFeedbackTimeLeft;

    private double mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    private Button btnNextRecipe;
    private Button btnPrevRecipe;

    private EditBox searchBar;

    private final List<SortButton> sortButtons = new ArrayList<>();

    private final String[] noIngredients;
    private final String[] noSelection;

    public KitchenScreen(KitchenMenu menu, Inventory playerInventory, Component displayName) {
        super(menu, playerInventory, displayName, DEFAULT_IMAGE_WIDTH, 174);

        noIngredients = I18n.get("gui.cookingforblockheads.no_ingredients").split("\\\\n");
        noSelection = I18n.get("gui.cookingforblockheads.no_selection").split("\\\\n");
    }

    @Override
    protected void init() {
        super.init();

        btnPrevRecipe = Button.builder(Component.literal("<"), it -> menu.nextRecipe(-1))
                .pos(width / 2 - 79, height / 2 - 51).size(13, 20).build();
        btnPrevRecipe.visible = false;
        addRenderableWidget(btnPrevRecipe);

        btnNextRecipe = Button.builder(Component.literal(">"), it -> menu.nextRecipe(1))
                .pos(width / 2 - 9, height / 2 - 51).size(13, 20).build();
        btnNextRecipe.visible = false;
        addRenderableWidget(btnNextRecipe);

        searchBar = new EditBox(minecraft.font, leftPos + imageWidth - 78, topPos - 5, 70, 10, searchBar, Component.empty());
        addRenderableWidget(searchBar);

        int yOffset = -80;

        sortButtons.clear();
        for (final var sortButton : CookingForBlockheadsRegistry.getSortButtons()) {
            SortButton button = new SortButton(width / 2 + 87, height / 2 + yOffset, sortButton, it -> {
                menu.setSortComparator(sortButton.getComparator(Minecraft.getInstance().player));
            });
            addRenderableWidget(button);
            sortButtons.add(button);

            yOffset += 20;
        }

        recalculateScrollBar();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (deltaY == 0) {
            return false;
        }

        if (menu.getSelectedRecipe() != null && mouseX >= leftPos + 24 && mouseY >= topPos + 20 && mouseX < leftPos + 78 && mouseY < topPos + 74) {
            Slot slot = ((AbstractContainerScreenAccessor) this).getHoveredSlot();
            if (slot instanceof CraftMatrixFakeSlot fakeSlot && fakeSlot.getVisibleStacks().size() > 1) {
                final var lockedInput = fakeSlot.scrollDisplayListAndLock(deltaY > 0 ? -1 : 1);
                menu.setLockedInput(fakeSlot.getIngredientIndex(), lockedInput);
            }
        } else {
            setCurrentOffset(deltaY > 0 ? currentOffset - 1 : currentOffset + 1);
        }

        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean result = super.mouseReleased(event);

        if (event.button() != -1 && mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }

        return result;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 1 && event.x() >= searchBar.getX() && event.x() < searchBar.getX() + searchBar.getWidth() && event.y() >= searchBar.getY() && event.y() < searchBar.getY() + searchBar.getHeight()) {
            searchBar.setValue("");
            menu.search(null);
            menu.updateCraftableSlots();
            setCurrentOffset(currentOffset);
            return true;
        } else {
            if (searchBar.mouseClicked(event, doubleClick)) {
                setFocused(searchBar);
                return true;
            } else {
                clearFocus();
            }
        }

        if (event.x() >= scrollBarXPos && event.x() <= scrollBarXPos + SCROLLBAR_WIDTH && event.y() >= scrollBarYPos && event.y() <= scrollBarYPos + scrollBarScaledHeight) {
            mouseClickY = event.y();
            indexWhenClicked = currentOffset;
        }

        Slot mouseSlot = ((AbstractContainerScreenAccessor) this).getHoveredSlot();
        if (mouseSlot instanceof CraftMatrixFakeSlot fakeSlot) {
            if (event.button() == 0) {
                ItemStack itemStack = mouseSlot.getItem();
                final var recipe = menu.findCraftableForResultItem(itemStack);
                if (recipe != null) {
                    menu.pushHistory();
                    menu.selectCraftable(recipe);
                    setCurrentOffset(menu.getRecipesForSelectionIndex());
                    setFocused(null);
                }
            } else if (event.button() == 1) {
                final var lockedInput = fakeSlot.toggleLock();
                menu.setLockedInput(fakeSlot.getIngredientIndex(), lockedInput);
            }
            return true;
        } else if (mouseSlot instanceof CraftableListingFakeSlot recipeFakeSlot) {
            if (event.hasAltDown()) {
                final var recipe = recipeFakeSlot.getCraftable();
                if (recipe != null) {
                    final var itemStack = recipe.itemStack();
                    final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                    Balm.networking().sendToServer(new ToggleFavoriteMessage(itemId, !isFavoriteItem(itemStack)));
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        boolean result = super.charTyped(event);

        menu.search(searchBar.getValue());
        menu.updateCraftableSlots();
        setCurrentOffset(currentOffset);

        return result;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape()) {
            minecraft.player.closeContainer();
            return true;
        }

        if (!searchBar.isFocused() && event.key() == GLFW.GLFW_KEY_BACKSPACE) {
            menu.popHistory();
            return true;
        }

        final var previousSearch = searchBar.getValue();
        if (searchBar.keyPressed(event) || searchBar.isFocused()) {
            if (!searchBar.getValue().equals(previousSearch)) {
                menu.search(searchBar.getValue());
                menu.updateCraftableSlots();
                setCurrentOffset(currentOffset);
            }
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        if (menu.isScrollOffsetDirty()) {
            setCurrentOffset(currentOffset);
            menu.setScrollOffsetDirty(false);
        }

        graphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, leftPos, topPos - 10, 0, 0, imageWidth, imageHeight + 10, 256, 256);

        if (mouseClickY != -1) {
            float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1,
                    (int) Math.ceil(menu.getItemListCount() / (float) VISIBLE_COLS) - VISIBLE_ROWS);
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        btnPrevRecipe.visible = menu.selectionHasRecipeVariants();
        btnPrevRecipe.active = menu.selectionHasPreviousRecipe();
        btnNextRecipe.visible = menu.selectionHasRecipeVariants();
        btnNextRecipe.active = menu.selectionHasNextRecipe();

        boolean hasRecipes = menu.getItemListCount() > 0;

        for (Button sortButton : sortButtons) {
            sortButton.active = hasRecipes;
        }

        Font font = minecraft.font;
        final var selection = menu.getSelectedRecipe();
        if (selection == null) {
            int curY = topPos + 79 / 2 - noSelection.length / 2 * font.lineHeight;
            for (String s : noSelection) {
                graphics.text(font, s, leftPos + 23 + 27 - font.width(s) / 2, curY, 0xFFFFFFFF, true);
                curY += font.lineHeight + 5;
            }
        } else if (selection.recipeDisplayEntry().display() instanceof FurnaceRecipeDisplay) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, leftPos + 23, topPos + 19, 54, 184, 54, 54, 256, 256);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, leftPos + 23, topPos + 19, 0, 184, 54, 54, 256, 256);
        }

        if (selection != null) {
            for (CraftMatrixFakeSlot slot : menu.getMatrixSlots()) {
                if (slot.isLocked() && slot.getVisibleStacks().size() > 1) {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, leftPos + slot.x, topPos + slot.y, 176, 60, 16, 16, 256, 256);
                }
            }
        }

        graphics.fill(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        if (menu.getItemListCount() == 0) {
            graphics.fill(leftPos + 97, topPos + 7, leftPos + 168, topPos + 85, 0xAA222222);
            int curY = topPos + 79 / 2 - noIngredients.length / 2 * font.lineHeight;
            for (String s : noIngredients) {
                graphics.text(font, s, leftPos + 97 + 36 - font.width(s) / 2, curY, 0xFFFFFFFF, true);
                curY += font.lineHeight + 5;
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        if (CookingForBlockheadsConfig.getActive().showIngredientIcon) {
            var poseStack = guiGraphics.pose();
            poseStack.pushMatrix();
            // TODO 1.21.6: poseStack.translate(0, 0, 300);
            for (Slot slot : menu.slots) {
                if (slot instanceof CraftableListingFakeSlot fakeSlot) {
                    if (slot.getItem().is(ModItemTags.INGREDIENTS)) {
                        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, slot.x, slot.y, 176, 76, 16, 16, 256, 256);
                    }

                    final var craftable = fakeSlot.getCraftable();
                    if (craftable != null && craftable.missingUtensils()) {
                        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, slot.x, slot.y, 176, 92, 16, 16, 256, 256);
                    }

                    if (craftable != null && isFavoriteItem(craftable.itemStack())) {
                        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, slot.x, slot.y, 176, 108, 16, 16, 256, 256);
                    }
                }
            }

            poseStack.popMatrix();
        }
    }

    private boolean isFavoriteItem(ItemStack itemStack) {
        return CookingForBlockheadsClient.isFavoriteItem(itemStack);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractContents(graphics, mouseX, mouseY, a);

        if (kitchenFeedback != null && kitchenFeedbackTimeLeft > 0) {
            float alpha = 1f;
            if (kitchenFeedbackTimeLeft < KITCHEN_FEEDBACK_HINT_TIME / 2f) {
                alpha = Math.max(0f, kitchenFeedbackTimeLeft / (KITCHEN_FEEDBACK_HINT_TIME / 2f));
            }
            // TODO 1.21.6: RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
            graphics.centeredText(font, kitchenFeedback, leftPos + 8 + 84 / 2, topPos + 18, 0xFFFFFFFF);
            // TODO 1.21.6: RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            kitchenFeedbackTimeLeft -= a;
        }

        var poseStack = graphics.pose();
        poseStack.pushMatrix();
        // TODO 1.21.6: poseStack.translate(0, 0, 300);
        for (Slot slot : menu.slots) {
            if (slot instanceof CraftMatrixFakeSlot fakeSlot) {
                if (fakeSlot.isMissing() && !slot.getItem().isEmpty()) {
                    graphics.fillGradient(leftPos + slot.x, topPos + slot.y, leftPos + slot.x + 16, topPos + slot.y + 16, 0x77FF4444, 0x77FF5555);
                }
            }
        }
        poseStack.popMatrix();

        for (CraftMatrixFakeSlot matrixSlot : menu.getMatrixSlots()) {
            matrixSlot.updateSlot(a);
        }
    }

    private void recalculateScrollBar() {
        int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f,
                ((float) VISIBLE_ROWS / (Math.ceil(menu.getItemListCount() / (float) VISIBLE_COLS)))));
        this.scrollBarXPos = leftPos + imageWidth - SCROLLBAR_WIDTH - 9;
        this.scrollBarYPos = topPos + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1,
                (int) Math.ceil((menu.getItemListCount() / (float) VISIBLE_COLS)) - VISIBLE_ROWS));
    }

    private void setCurrentOffset(int currentOffset) {
        this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(menu.getItemListCount() / (float) VISIBLE_COLS) - VISIBLE_ROWS));

        menu.setScrollOffset(this.currentOffset);

        recalculateScrollBar();
    }

    public List<Button> getSortingButtons() {
        return new ArrayList<>(sortButtons);
    }

    public void displayKitchenFeedback(Component component) {
        kitchenFeedback = component;
        kitchenFeedbackTimeLeft = KITCHEN_FEEDBACK_HINT_TIME;
    }
}
