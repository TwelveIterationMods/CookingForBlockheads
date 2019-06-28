package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.blay09.mods.cookingforblockheads.ModConfig;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.GuiRecipeBook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

@JEIPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        // Register cow jar recipe
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.cowJar), CowJarRecipeCategory.UID);

        if (ModConfig.general.cowJarEnabled) {
            registry.addRecipes(ImmutableList.of(new CowJarRecipe()), CowJarRecipeCategory.UID);
        }

        // Do not put JEI items behind the sorting buttons
        registry.addAdvancedGuiHandlers(new IAdvancedGuiHandler<GuiRecipeBook>() {
            @Override
            public Class<GuiRecipeBook> getGuiContainerClass() {
                return GuiRecipeBook.class;
            }

            @Override
            public List<Rectangle> getGuiExtraAreas(GuiRecipeBook guiContainer) {
                List<Rectangle> list = Lists.newArrayList();
                for (GuiButton button : guiContainer.getSortingButtons()) {
                    list.add(new Rectangle(button.x, button.y, button.width, button.height));
                }
                return list;
            }

            @Nullable
            @Override
            public Object getIngredientUnderMouse(GuiRecipeBook guiContainer, int mouseX, int mouseY) {
                return null;
            }
        });

        // Blacklist the cutting board block from JEI (can't remove the item block yet since it's already been released)
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.cuttingBoard));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new CowJarRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }
}
