package net.blay09.mods.cookingforblockheads.client.gui;

import java.io.IOException;
import java.util.List;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ModConfig;
import net.blay09.mods.cookingforblockheads.api.ISortButton;
import net.blay09.mods.cookingforblockheads.api.RecipeStatus;
import net.blay09.mods.cookingforblockheads.container.ContainerRecipeBook;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotCraftMatrix;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.registry.CookingRegistry;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithIngredients;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;
import yalter.mousetweaks.api.MouseTweaksIgnore;

@MouseTweaksIgnore
public class GuiRecipeBook extends GuiContainer {

	private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
	private static final int SCROLLBAR_Y = 8;
	private static final int SCROLLBAR_WIDTH = 7;
	private static final int SCROLLBAR_HEIGHT = 77;

	private static final ResourceLocation guiTexture = new ResourceLocation(CookingForBlockheads.MOD_ID, "textures/gui/gui.png");
	private static final int VISIBLE_ROWS = 4;

	private final ContainerRecipeBook container;
	private boolean isEventHandler;
	private int scrollBarScaledHeight;
	private int scrollBarXPos;
	private int scrollBarYPos;
	private int currentOffset;

	private int mouseClickY = -1;
	private int indexWhenClicked;
	private int lastNumberOfMoves;

	private GuiButton btnNextRecipe;
	private GuiButton btnPrevRecipe;

	private GuiTextField searchBar;

	private final List<GuiButtonSort> sortButtons = Lists.newArrayList();
	
	private final String[] noIngredients;
	private final String[] noSelection;

	public GuiRecipeBook(ContainerRecipeBook container) {
		super(container);
		this.container = container;

		noIngredients = I18n.format("gui." + CookingForBlockheads.MOD_ID + ":no_ingredients").split("\\\\n");
		noSelection = I18n.format("gui." + CookingForBlockheads.MOD_ID + ":no_selection").split("\\\\n");
	}

	@Override
	public void initGui() {
		ySize = 174;
		super.initGui();

		btnPrevRecipe = new GuiButton(0, width / 2 - 79, height / 2 - 51, 13, 20, "<");
		btnPrevRecipe.visible = false;
		buttonList.add(btnPrevRecipe);

		btnNextRecipe = new GuiButton(1, width / 2 - 9, height / 2 - 51, 13, 20, ">");
		btnNextRecipe.visible = false;
		buttonList.add(btnNextRecipe);

		searchBar = new GuiTextField(2, fontRenderer, guiLeft + xSize - 78, guiTop - 5, 70, 10);
//		searchBar.setFocused(true);
		
		int yOffset = -80;
		int id = 3; 
		
		for (ISortButton button : CookingRegistry.getSortButtons()) {
			GuiButtonSort sortButton = new GuiButtonSort(id++, width / 2 + 87, height / 2 + yOffset, button);
			buttonList.add(sortButton);
			sortButtons.add(sortButton);
			
			yOffset += 20;
		}
		
		if(!isEventHandler) {
			MinecraftForge.EVENT_BUS.register(this);
			isEventHandler = true;
		}

		recalculateScrollBar();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if(button == btnPrevRecipe) {
			container.nextSubRecipe(-1);
		} else if(button == btnNextRecipe) {
			container.nextSubRecipe(1);
		}
		
		for (GuiButton sortButton : this.sortButtons) {
			if (sortButton instanceof GuiButtonSort && button == sortButton) {
				container.setSortComparator(((GuiButtonSort)sortButton).getComparator(Minecraft.getMinecraft().player));
			}
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta == 0) {
			return;
		}
		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		if(container.getSelection() != null && mouseX >= guiLeft + 7 && mouseY >= guiTop + 17 && mouseX < guiLeft + 92 && mouseY < guiTop + 95) {
			Slot slot = getSlotUnderMouse();
			if(slot instanceof FakeSlotCraftMatrix && ((FakeSlotCraftMatrix) slot).getVisibleStacks().size() > 1) {
				((FakeSlotCraftMatrix) slot).scrollDisplayList(delta > 0 ? -1 : 1);
			}
		} else {
			setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (state != -1 && mouseClickY != -1) {
			mouseClickY = -1;
			indexWhenClicked = 0;
			lastNumberOfMoves = 0;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		super.mouseClicked(mouseX, mouseY, button);
		if(button == 1 && mouseX >= searchBar.x && mouseX < searchBar.x + searchBar.width && mouseY >= searchBar.y && mouseY < searchBar.y + searchBar.height) {
			searchBar.setText("");
			container.search(null);
			container.populateRecipeSlots();
			setCurrentOffset(currentOffset);
		} else {
			searchBar.mouseClicked(mouseX, mouseY, button);
		}
		if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
			mouseClickY = mouseY;
			indexWhenClicked = currentOffset;
		}
	}

	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		if(searchBar.textboxKeyTyped(c, keyCode)) {
			container.search(searchBar.getText());
			container.populateRecipeSlots();
			setCurrentOffset(currentOffset);
		} else {
			super.keyTyped(c, keyCode);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		if(container.isDirty()) {
			setCurrentOffset(currentOffset);
			container.setDirty(false);
		}

		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(guiTexture);
		drawTexturedModalRect(guiLeft, guiTop - 10, 0, 0, xSize, ySize + 10);

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
		btnPrevRecipe.enabled = container.getSelectionIndex() > 0;
		btnNextRecipe.visible = container.hasVariants();
		btnNextRecipe.enabled = container.getSelectionIndex() < container.getRecipeCount() - 1;

		boolean hasRecipes = container.getItemListCount() > 0;

		for (GuiButton sortButton : sortButtons) {
			sortButton.enabled = hasRecipes;
		}

		GlStateManager.color(1f, 1f, 1f, 1f);

		if(ModConfig.client.showIngredientIcon) {
			float prevZLevel = zLevel;
			zLevel = 300f;
			for (Slot slot : inventorySlots.inventorySlots) {
				if (CookingRegistry.isNonFoodRecipe(slot.getStack())) {
					drawTexturedModalRect(guiLeft + slot.xPos, guiTop + slot.yPos, 176, 76, 16, 16);
				}
			}
			zLevel = prevZLevel;
		}

		FoodRecipeWithIngredients selection = container.getSelection();
		if(selection == null) {
			int curY = guiTop + 79 / 2 - noSelection.length / 2 * fontRenderer.FONT_HEIGHT;
			for(String s : noSelection) {
				fontRenderer.drawStringWithShadow(s, guiLeft + 23 + 27 - fontRenderer.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRenderer.FONT_HEIGHT + 5;
			}
		} else if(selection.getRecipeType() == RecipeType.SMELTING) {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 54, 184, 54, 54);
		} else {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 0, 184, 54, 54);
		}

		if(selection != null) {
			for (FakeSlotCraftMatrix slot : container.getCraftingMatrixSlots()) {
				if (slot.isLocked() && slot.getVisibleStacks().size() > 1) {
					drawTexturedModalRect(guiLeft + slot.xPos, guiTop + slot.yPos, 176, 60, 16, 16);
				}
			}
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

		if(container.getItemListCount() == 0) {
			GuiContainer.drawRect(guiLeft + 97, guiTop + 7, guiLeft + 168, guiTop + 85, 0xAA222222);
			int curY = guiTop + 79 / 2 - noIngredients.length / 2 * fontRenderer.FONT_HEIGHT;
			for(String s : noIngredients) {
				fontRenderer.drawStringWithShadow(s, guiLeft + 97 + 36 - fontRenderer.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRenderer.FONT_HEIGHT + 5;
			}
		}



		searchBar.drawTextBox();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		container.updateSlots(partialTicks);

		for (GuiButton sortButton : this.sortButtons) {
			if (sortButton instanceof GuiButtonSort && sortButton.isMouseOver() && sortButton.enabled) {
				drawHoveringText(((GuiButtonSort)sortButton).getTooltipLines(), mouseX, mouseY);
			}
		}

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(isEventHandler) {
			MinecraftForge.EVENT_BUS.unregister(this);
			isEventHandler = false;
		}
	}

	public void recalculateScrollBar() {
		int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
		this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(container.getItemListCount() / 3f)))));
		this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
		this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((container.getItemListCount() / 3f)) - VISIBLE_ROWS));
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getItemListCount() / 3f) - VISIBLE_ROWS));

		container.setScrollOffset(this.currentOffset);

		recalculateScrollBar();
	}

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		Slot hoverSlot = getSlotUnderMouse();
		if(hoverSlot instanceof FakeSlotRecipe && event.getItemStack() == hoverSlot.getStack()) {
			FakeSlotRecipe slotRecipe = (FakeSlotRecipe) hoverSlot;
			if(container.isSelectedSlot(slotRecipe) && container.isAllowCrafting()) {
				FoodRecipeWithIngredients subRecipe = container.getSelection();
				if(subRecipe != null && subRecipe.getRecipeType() == RecipeType.SMELTING) {
					if(!container.hasOven()) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missing_oven"));
					} else {
						if (isShiftKeyDown()) {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_smelt_stack"));
						} else {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_smelt_one"));
						}
					}
				} else if(slotRecipe.getRecipe() != null) {
					if (slotRecipe.getRecipe().getStatus() == RecipeStatus.MISSING_TOOLS) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missing_tools"));
					} else if(slotRecipe.getRecipe().getStatus() == RecipeStatus.MISSING_INGREDIENTS) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missing_ingredients"));
					} else {
						if (isShiftKeyDown()) {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_craft_stack"));
						} else {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_craft_one"));
						}
					}
				}
			} else {
				event.getToolTip().add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_see_recipe"));
			}
		} else if(hoverSlot instanceof FakeSlotCraftMatrix && event.getItemStack() == hoverSlot.getStack()) {
			if(((FakeSlotCraftMatrix) hoverSlot).getVisibleStacks().size() > 1) {
				if(((FakeSlotCraftMatrix) hoverSlot).isLocked()) {
					event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_unlock"));
				} else {
					event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":click_to_lock"));
				}
				event.getToolTip().add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":scroll_to_switch"));
			}
		}
	}

	public GuiButton[] getSortingButtons() {
		return sortButtons.toArray(new GuiButton[0]);
	}

}
