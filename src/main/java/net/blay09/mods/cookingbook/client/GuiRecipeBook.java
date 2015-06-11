package net.blay09.mods.cookingbook.client;

import net.blay09.mods.cookingbook.container.ContainerRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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

	public GuiRecipeBook(ContainerRecipeBook container) {
		super(container);
		this.container = container;
	}

	@Override
	public void initGui() {
		ySize = 174;
		super.initGui();
		recalculateScrollBar();
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

		if(container.isFurnaceMode()) {
			drawTexturedModalRect(guiLeft + 22, guiTop + 19, 54, 174, 54, 54);
		} else {
			drawTexturedModalRect(guiLeft + 22, guiTop + 19, 0, 174, 54, 54);
		}

		GuiContainer.drawRect(scrollBarXPos, scrollBarYPos, scrollBarXPos + SCROLLBAR_WIDTH, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = Math.max(0, Math.min(currentOffset, (int) Math.ceil(container.getAvailableRecipeCount() / 3f) - VISIBLE_ROWS));

		container.setScrollOffset(this.currentOffset);

		recalculateScrollBar();
	}
}
