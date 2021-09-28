package net.blay09.mods.cookingforblockheads.compat.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.gui.screen.RecipeBookScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@JeiPlugin
public class JEIAddon implements IModPlugin {

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.cowJar), CowJarRecipeCategory.UID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (CookingForBlockheadsConfig.COMMON.cowJarEnabled.get()) {
            registration.addRecipes(ImmutableList.of(new CowJarRecipe()), CowJarRecipeCategory.UID);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(RecipeBookScreen.class, new IGuiContainerHandler<RecipeBookScreen>() {
            @Override
            public List<Rectangle2d> getGuiExtraAreas(RecipeBookScreen containerScreen) {
                List<Rectangle2d> list = Lists.newArrayList();
                for (Button button : containerScreen.getSortingButtons()) {
                    list.add(new Rectangle2d(button.x, button.y, button.getWidth(), button.getHeightRealms()));
                }

                return list;
            }
        });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("cookingforblockheads", "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CowJarRecipeCategory(guiHelper));
    }

}
