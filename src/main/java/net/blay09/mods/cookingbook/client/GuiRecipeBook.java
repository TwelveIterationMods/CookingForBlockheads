package net.blay09.mods.cookingbook.client;

import net.blay09.mods.cookingbook.container.ComparatorHunger;
import net.blay09.mods.cookingbook.container.ComparatorName;
import net.blay09.mods.cookingbook.container.ComparatorSaturation;
import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiRecipeBook extends GuiContainer {

	private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
	private static final int SCROLLBAR_Y = 8;
	private static final int SCROLLBAR_WIDTH = 7;
	private static final int SCROLLBAR_HEIGHT = 77;

	private static final ResourceLocation guiTexture = new ResourceLocation("cookingbook", "textures/gui/gui.png");
	private static final int VISIBLE_ROWS = 4;

	private final ContainerRecipeBook container;
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

	public GuiRecipeBook(ContainerRecipeBook container) {
		super(container);
		this.container = container;
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

		btnSortName = new GuiButtonSort(this, 2, width / 2 + 87, height / 2 - 80, 196, "cookingbook:sort_by_name.tooltip");
		buttonList.add(btnSortName);

		btnSortHunger = new GuiButtonSort(this, 3, width / 2 + 87, height / 2 - 60, 216, "cookingbook:sort_by_hunger.tooltip");
		buttonList.add(btnSortHunger);

		btnSortSaturation = new GuiButtonSort(this, 4, width / 2 + 87, height / 2 - 40, 236, "cookingbook:sort_by_saturation.tooltip");
		buttonList.add(btnSortSaturation);

		recalculateScrollBar();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);

		if(button == btnPrevRecipe) {
			container.prevRecipe();
		} else if(button == btnNextRecipe) {
			container.nextRecipe();
		} else if(button == btnSortName) {
			container.sortRecipes(new ComparatorName());
		} else if(button == btnSortHunger) {
			container.sortRecipes(new ComparatorHunger());
		} else if(button == btnSortSaturation) {
			container.sortRecipes(new ComparatorSaturation());
		}
	}

	public void recalculateScrollBar()
	{
		int scrollBarTotalHeight = SCROLLBAR_HEIGHT - 1;
		this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1f, ((float) VISIBLE_ROWS / (Math.round(container.getAvailableRecipeCount() / 3f)))));
		this.scrollBarXPos = guiLeft + xSize - SCROLLBAR_WIDTH - 9;
		this.scrollBarYPos = guiTop + SCROLLBAR_Y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentOffset / Math.max(1, (container.getAvailableRecipeCount() / 3) - VISIBLE_ROWS));
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
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
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
		GL11.glColor4f(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(guiTexture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (mouseClickY != -1) {
			float pixelsPerFilter = (SCROLLBAR_HEIGHT - scrollBarScaledHeight) / Math.max(1, (container.getAvailableRecipeCount() / 3) - VISIBLE_ROWS);
			if (pixelsPerFilter != 0) {
				int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
				if (numberOfFiltersMoved != lastNumberOfMoves) {
					setCurrentOffset(indexWhenClicked + numberOfFiltersMoved);
					lastNumberOfMoves = numberOfFiltersMoved;
				}
			}
		}

		if(container.hasVariants()) {
			btnPrevRecipe.visible = true;
			btnNextRecipe.visible = true;
		} else {
			btnPrevRecipe.visible = false;
			btnNextRecipe.visible = false;
		}

		if(container.isFurnaceMode()) {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 54, 174, 54, 54);
		} else {
			drawTexturedModalRect(guiLeft + 23, guiTop + 19, 0, 174, 54, 54);
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		if(btnSortName.isMouseOver()) {
			drawHoveringText(btnSortName.getTooltipLines(), mouseX, mouseY);
		} else if(btnSortHunger.isMouseOver()) {
			drawHoveringText(btnSortHunger.getTooltipLines(), mouseX, mouseY);
		} else if(btnSortSaturation.isMouseOver()) {
			drawHoveringText(btnSortSaturation.getTooltipLines(), mouseX, mouseY);
		}
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getAvailableRecipeCount() / 3f) - VISIBLE_ROWS));

		container.setScrollOffset(this.currentOffset);

		recalculateScrollBar();
	}

}
