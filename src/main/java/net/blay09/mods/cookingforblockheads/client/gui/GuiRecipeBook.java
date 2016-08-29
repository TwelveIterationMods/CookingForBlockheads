package net.blay09.mods.cookingforblockheads.client.gui;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.container.*;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorHunger;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorName;
import net.blay09.mods.cookingforblockheads.container.comparator.ComparatorSaturation;
import net.blay09.mods.cookingforblockheads.container.slot.FakeSlotRecipe;
import net.blay09.mods.cookingforblockheads.registry.FoodRecipeWithStatus;
import net.blay09.mods.cookingforblockheads.registry.RecipeStatus;
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

import java.io.IOException;

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

	private final GuiButtonSort[] sortButtons = new GuiButtonSort[3];
	private GuiButtonSort btnSortName;
	private GuiButtonSort btnSortHunger;
	private GuiButtonSort btnSortSaturation;

	private final String[] noIngredients;
	private final String[] noSelection;

	public GuiRecipeBook(ContainerRecipeBook container) {
		super(container);
		this.container = container;

		noIngredients = I18n.format("gui." + CookingForBlockheads.MOD_ID + ":noIngredients").split("\\\\n");
		noSelection = I18n.format("gui." + CookingForBlockheads.MOD_ID + ":noSelection").split("\\\\n");
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

		btnSortName = new GuiButtonSort(2, width / 2 + 87, height / 2 - 80, 196, "tooltip." + CookingForBlockheads.MOD_ID + ":sortByName");
		buttonList.add(btnSortName);
		sortButtons[0] = btnSortName;

		btnSortHunger = new GuiButtonSort(3, width / 2 + 87, height / 2 - 60, 216, "tooltip." + CookingForBlockheads.MOD_ID + ":sortByHunger");
		buttonList.add(btnSortHunger);
		sortButtons[1] = btnSortHunger;

		btnSortSaturation = new GuiButtonSort(4, width / 2 + 87, height / 2 - 40, 236, "tooltip." + CookingForBlockheads.MOD_ID + ":sortBySaturation");
		buttonList.add(btnSortSaturation);
		sortButtons[2] = btnSortSaturation;

		searchBar = new GuiTextField(5, fontRendererObj, guiLeft + xSize - 78, guiTop - 5, 70, 10);
//		searchBar.setFocused(true);

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
//			container.prevRecipe();
		} else if(button == btnNextRecipe) {
//			container.nextRecipe();
		} else if(button == btnSortName) {
			container.setSortComparator(new ComparatorName());
		} else if(button == btnSortHunger) {
			container.setSortComparator(new ComparatorHunger(Minecraft.getMinecraft().thePlayer));
		} else if(button == btnSortSaturation) {
			container.setSortComparator(new ComparatorSaturation(Minecraft.getMinecraft().thePlayer));
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta == 0) {
			return;
		}
		setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
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
		if(button == 1 && mouseX >= searchBar.xPosition && mouseX < searchBar.xPosition + searchBar.width && mouseY >= searchBar.yPosition && mouseY < searchBar.yPosition + searchBar.height) {
			searchBar.setText("");
			container.search(null);
			recalculateScrollBar();
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
			recalculateScrollBar();
		} else {
			super.keyTyped(c, keyCode);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		if(container.isDirty()) {
			recalculateScrollBar();
			container.setDirty(false);
		}

		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(guiTexture);
		drawTexturedModalRect(guiLeft, guiTop - 10, 0, 0, xSize, ySize + 10);

		if (mouseClickY != -1) {
			float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(container.getRecipeCount() / 3f) - VISIBLE_ROWS);
			if (pixelsPerFilter != 0) {
				int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
				if (numberOfFiltersMoved != lastNumberOfMoves) {
					setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
					lastNumberOfMoves = numberOfFiltersMoved;
				}
			}
		}

//		boolean hasVariants = container.hasVariants();
//		btnPrevRecipe.visible = hasVariants;
//		btnNextRecipe.visible = hasVariants;

		boolean hasRecipes = container.getRecipeCount() > 0;
		btnSortName.enabled = hasRecipes;
		btnSortHunger.enabled = hasRecipes;
		btnSortSaturation.enabled = hasRecipes;

		FoodRecipeWithStatus selection = container.getSelection();
		if(selection == null) {
			int curY = guiTop + 79 / 2 - noSelection.length / 2 * fontRendererObj.FONT_HEIGHT;
			for(String s : noSelection) {
				fontRendererObj.drawStringWithShadow(s, guiLeft + 23 + 27 - fontRendererObj.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRendererObj.FONT_HEIGHT + 5;
			}
		} else if(selection.getType() == RecipeType.SMELTING) {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 54, 184, 54, 54);
		} else {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 0, 184, 54, 54);
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

		if(container.getRecipeCount() == 0) {
			GuiContainer.drawRect(guiLeft + 97, guiTop + 7, guiLeft + 168, guiTop + 85, 0xAA222222);
			int curY = guiTop + 79 / 2 - noIngredients.length / 2 * fontRendererObj.FONT_HEIGHT;
			for(String s : noIngredients) {
				fontRendererObj.drawStringWithShadow(s, guiLeft + 97 + 36 - fontRendererObj.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRendererObj.FONT_HEIGHT + 5;
			}
		}

		searchBar.drawTextBox();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		if(btnSortName.isMouseOver() && btnSortName.enabled) {
			drawHoveringText(btnSortName.getTooltipLines(), mouseX, mouseY);
		} else if(btnSortHunger.isMouseOver() && btnSortHunger.enabled) {
			drawHoveringText(btnSortHunger.getTooltipLines(), mouseX, mouseY);
		} else if(btnSortSaturation.isMouseOver() && btnSortSaturation.enabled) {
			drawHoveringText(btnSortSaturation.getTooltipLines(), mouseX, mouseY);
		}
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
		this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(container.getRecipeCount() / 3f)))));
		this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
		this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((container.getRecipeCount() / 3f)) - VISIBLE_ROWS));
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getRecipeCount() / 3f) - VISIBLE_ROWS));

		container.setScrollOffset(this.currentOffset);

		recalculateScrollBar();
	}

	@SubscribeEvent
	@SuppressWarnings("unused")
	public void onItemTooltip(ItemTooltipEvent event) {
		Slot hoverSlot = getSlotUnderMouse();
		if(hoverSlot instanceof FakeSlotRecipe && event.getItemStack() == hoverSlot.getStack()) {
			FakeSlotRecipe slotRecipe = (FakeSlotRecipe) hoverSlot;
			if(container.isSelectedSlot(slotRecipe) && container.isAllowCrafting()) {
				if(slotRecipe.getRecipe().getType() == RecipeType.SMELTING) {
					if(!container.hasOven()) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missingOven"));
					} else {
						if (isShiftKeyDown()) {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":clickToSmeltStack"));
						} else {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":clickToSmeltOne"));
						}
					}
				} else {
					if (slotRecipe.getRecipe().getStatus() == RecipeStatus.MISSING_TOOLS) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missingTools"));
					} else if(slotRecipe.getRecipe().getStatus() == RecipeStatus.MISSING_INGREDIENTS) {
						event.getToolTip().add(TextFormatting.RED + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":missingIngredients"));
					} else {
						if (isShiftKeyDown()) {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":clickToCraftStack"));
						} else {
							event.getToolTip().add(TextFormatting.GREEN + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":clickToCraftOne"));
						}
					}
				}
			} else {
				event.getToolTip().add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":clickToSeeRecipe"));
			}
		}
	}

	public GuiButton[] getSortingButtons() {
		return sortButtons;
	}
}
