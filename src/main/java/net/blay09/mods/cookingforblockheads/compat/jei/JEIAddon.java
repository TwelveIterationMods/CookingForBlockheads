package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.blay09.mods.cookingforblockheads.client.gui.GuiRecipeBook;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.List;

@JEIPlugin
public class JEIAddon extends BlankModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		// Register cow jar recipe
		registry.addRecipeCategories(new CowJarRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new CowJarRecipeHandler());
		registry.addRecipes(ImmutableList.of(new CowJarRecipe()));

		// Do not put JEI items behind the sorting buttons
		registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiRecipeBook>() {
			@Nonnull
			@Override
			public Class<GuiRecipeBook> getGuiContainerClass() {
				return GuiRecipeBook.class;
			}

			@Nullable
			@Override
			public List<Rectangle> getGuiExtraAreas(GuiRecipeBook guiContainer) {
				List<Rectangle> list = Lists.newArrayList();
				for(GuiButton button : guiContainer.getSortingButtons()) {
					list.add(new Rectangle(button.xPosition, button.yPosition, button.width, button.height));
				}
				return list;
			}
		});
	}

}
