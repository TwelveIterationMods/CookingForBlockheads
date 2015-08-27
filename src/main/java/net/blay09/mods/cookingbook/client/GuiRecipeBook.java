package net.blay09.mods.cookingbook.client;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.cookingbook.container.*;
import net.blay09.mods.cookingbook.network.MessageSort;
import net.blay09.mods.cookingbook.network.MessageSwitchRecipe;
import net.blay09.mods.cookingbook.network.NetworkHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import yalter.mousetweaks.api.IMTModGuiContainer;

@Optional.Interface(modid = "MouseTweaks", iface = "yalter.mousetweaks.api.IMTModGuiContainer", striprefs = true)
public class GuiRecipeBook extends GuiContainer implements IMTModGuiContainer {

	private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
	private static final int SCROLLBAR_Y = 8;
	private static final int SCROLLBAR_WIDTH = 7;
	private static final int SCROLLBAR_HEIGHT = 77;

	private static final ResourceLocation guiTexture = new ResourceLocation("cookingbook", "textures/gui/gui.png");
	private static final int VISIBLE_ROWS = 4;

	private final ContainerRecipeBook container;
	private boolean registered;
	private int scrollBarScaledHeight;
	private int scrollBarXPos;
	private int scrollBarYPos;
	private int currentOffset;

	private int mouseClickY = -1;
	private int indexWhenClicked;
	private int lastNumberOfMoves;

	private GuiButton btnNextRecipe;
	private GuiButton btnPrevRecipe;

	private GuiButtonSort btnSortName;
	private GuiButtonSort btnSortHunger;
	private GuiButtonSort btnSortSaturation;

	private final String[] noIngredients;
	private final String[] noSelection;

	private Slot hoverSlot;

	public GuiRecipeBook(ContainerRecipeBook container) {
		super(container);
		this.container = container;

		noIngredients = StatCollector.translateToLocal("cookingbook:no_ingredients").split("\\\\n");
		noSelection = StatCollector.translateToLocal("cookingbook:no_selection").split("\\\\n");
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

		btnSortName = new GuiButtonSort(2, width / 2 + 87, height / 2 - 80, 196, "cookingbook:sort_by_name.tooltip");
		buttonList.add(btnSortName);

		btnSortHunger = new GuiButtonSort(3, width / 2 + 87, height / 2 - 60, 216, "cookingbook:sort_by_hunger.tooltip");
		buttonList.add(btnSortHunger);

		btnSortSaturation = new GuiButtonSort(4, width / 2 + 87, height / 2 - 40, 236, "cookingbook:sort_by_saturation.tooltip");
		buttonList.add(btnSortSaturation);

		if(!registered) {
			MinecraftForge.EVENT_BUS.register(this);
			registered = true;
		}

		recalculateScrollBar();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if(registered) {
			MinecraftForge.EVENT_BUS.unregister(this);
			registered = false;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);

		if(button == btnPrevRecipe) {
			container.prevRecipe();
			NetworkHandler.instance.sendToServer(new MessageSwitchRecipe(-1));
		} else if(button == btnNextRecipe) {
			container.nextRecipe();
			NetworkHandler.instance.sendToServer(new MessageSwitchRecipe(1));
		} else if(button == btnSortName) {
			NetworkHandler.instance.sendToServer(new MessageSort(0));
		} else if(button == btnSortHunger) {
			NetworkHandler.instance.sendToServer(new MessageSort(1));
		} else if(button == btnSortSaturation) {
			NetworkHandler.instance.sendToServer(new MessageSort(2));
		}
	}

	public void recalculateScrollBar() {
		int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
		this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.ceil(container.getAvailableRecipeCount() / 3f)))));
		this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
		this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (int) Math.ceil((container.getAvailableRecipeCount() / 3f)) - VISIBLE_ROWS));
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int delta = Mouse.getEventDWheel();
		if (delta == 0) {
			return;
		}
		setCurrentOffset(delta > 0 ? currentOffset - 1 : currentOffset + 1);
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		super.mouseMovedOrUp(mouseX, mouseY, state);
		if (state != -1 && mouseClickY != -1) {
			mouseClickY = -1;
			indexWhenClicked = 0;
			lastNumberOfMoves = 0;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
		if (mouseX >= scrollBarXPos && mouseX <= scrollBarXPos + SCROLLBAR_WIDTH && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
			mouseClickY = mouseY;
			indexWhenClicked = currentOffset;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		if(container.isRecipeListDirty()) {
			setCurrentOffset(currentOffset);
			container.markDirty(false);
		}

		GL11.glColor4f(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(guiTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (mouseClickY != -1) {
			float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / (float) Math.max(1, (int) Math.ceil(container.getAvailableRecipeCount() / 3f) - VISIBLE_ROWS);
			if (pixelsPerFilter != 0) {
				int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
				if (numberOfFiltersMoved != lastNumberOfMoves) {
					setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
					lastNumberOfMoves = numberOfFiltersMoved;
				}
			}
		}

		boolean hasVariants = container.hasVariants();
		btnPrevRecipe.visible = hasVariants;
		btnNextRecipe.visible = hasVariants;

		boolean hasRecipes = container.getAvailableRecipeCount() > 0;
		btnSortName.enabled = hasRecipes;
		btnSortHunger.enabled = hasRecipes;
		btnSortSaturation.enabled = hasRecipes;

		if(!container.hasSelection()) {
			int curY = guiTop + 79 / 2 - noSelection.length / 2 * fontRendererObj.FONT_HEIGHT;
			for(String s : noSelection) {
				fontRendererObj.drawStringWithShadow(s, guiLeft + 23 + 27 - fontRendererObj.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRendererObj.FONT_HEIGHT + 5;
			}
		} else if(container.isFurnaceRecipe()) {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 54, 174, 54, 54);
		} else {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 0, 174, 54, 54);
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

		if(container.getAvailableRecipeCount() == 0) {
			GuiContainer.drawRect(guiLeft + 97, guiTop + 7, guiLeft + 168, guiTop + 85, 0xAA222222);
			int curY = guiTop + 79 / 2 - noIngredients.length / 2 * fontRendererObj.FONT_HEIGHT;
			for(String s : noIngredients) {
				fontRendererObj.drawStringWithShadow(s, guiLeft + 97 + 36 - fontRendererObj.getStringWidth(s) / 2, curY, 0xFFFFFFFF);
				curY += fontRendererObj.FONT_HEIGHT + 5;
			}
		}

		hoverSlot = getSlotAtPosition(mouseX, mouseY);
	}

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		if(hoverSlot != null && hoverSlot instanceof SlotRecipe && event.itemStack == hoverSlot.getStack()) {
			if(container.gotRecipeInfo() && container.canClickCraft((currentOffset * 3) + hoverSlot.getSlotIndex())) {
				if (container.isMissingTools()) {
					event.toolTip.add("\u00a7c" + I18n.format("cookingbook:missing_tools"));
				} else {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						event.toolTip.add("\u00a7a" + I18n.format("cookingbook:click_to_craft_all"));
					} else {
						event.toolTip.add("\u00a7a" + I18n.format("cookingbook:click_to_craft_one"));
					}
				}
			} else if(container.gotRecipeInfo() && container.canClickSmelt((currentOffset * 3) + hoverSlot.getSlotIndex())) {
				if(container.isMissingOven()) {
					event.toolTip.add("\u00a7c" + I18n.format("cookingbook:missing_oven"));
				} else {
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						event.toolTip.add("\u00a7a" + I18n.format("cookingbook:click_to_smelt_all"));
					} else {
						event.toolTip.add("\u00a7a" + I18n.format("cookingbook:click_to_smelt_one"));
					}
				}
			} else {
				event.toolTip.add("\u00a7e" + I18n.format("cookingbook:click_to_see_recipe"));
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		if(btnSortName.func_146115_a() && btnSortName.enabled) { // isMouseOver
			func_146283_a(btnSortName.getTooltipLines(), mouseX, mouseY); // drawHoveringText
		} else if(btnSortHunger.func_146115_a() && btnSortHunger.enabled) {// isMouseOver
			func_146283_a(btnSortHunger.getTooltipLines(), mouseX, mouseY);// drawHoveringText
		} else if(btnSortSaturation.func_146115_a() && btnSortSaturation.enabled) {// isMouseOver
			func_146283_a(btnSortSaturation.getTooltipLines(), mouseX, mouseY);// drawHoveringText
		}
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getAvailableRecipeCount() / 3f) - VISIBLE_ROWS));

		container.setScrollOffset(this.currentOffset);

		recalculateScrollBar();
	}

	private Slot getSlotAtPosition(int x, int y) {
		for (int k = 0; k < inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = (Slot) inventorySlots.inventorySlots.get(k);

			if(isMouseOverSlot(slot, x, y)) {
				return slot;
			}
		}
		return null;
	}

	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
		return func_146978_c(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY);
	}

	@Override
	public int getAPIVersion() {
		return 1;
	}

	@Override
	public String getModName() {
		return "Cooking for Blockheads";
	}

	@Override
	public boolean isMouseTweaksDisabled() {
		return true;
	}

	@Override
	public boolean isWheelTweakDisabled() {
		return true;
	}

	@Override
	public boolean isCraftingOutputSlot(Object o, Object o1) {
		return false;
	}

	@Override
	public Object getModContainer() {
		return inventorySlots;
	}

	@Override
	public int getModSlotCount(Object o) {
		return inventorySlots.inventorySlots.size();
	}

	@Override
	public Object getModSlot(Object o, int i) {
		return inventorySlots.getSlot(i);
	}

	@Override
	public Object getModSelectedSlot(Object o, int i) {
		return null;
	}

	@Override
	public void clickModSlot(Object o, Object o1, int i, boolean b) {}

	@Override
	public void disableRMBDragIfRequired(Object o, Object o1, boolean b) {}
}
